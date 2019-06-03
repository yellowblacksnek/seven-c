package ru.snek;

import ru.snek.Collection.Malefactor;
import ru.snek.Collection.MapValuesComparator;
import ru.snek.Collection.MapWrapperUtils;

import java.io.*;
import java.net.*;
import java.util.Map;

import static ru.snek.Command.Commands.*;
import static ru.snek.Response.Status.*;
import static ru.snek.FileInteractor.*;
import static ru.snek.Logger.*;
import static ru.snek.Printer.*;
import static ru.snek.Utils.*;

public class Client {
    private Connection con;
    private volatile boolean authorised = false;
    private volatile String token;
    private volatile boolean connected;
    private volatile boolean onRegister = false;
    //private ServerListener listener;

    public Client()  {}

    public void start(String addr, int port) {
        InetAddress address = null;
        try {
            if(addr == null) address = InetAddress.getLocalHost();
            else address = InetAddress.getByName(addr);
        } catch (UnknownHostException e) {
            errprintln("Не удалось определить адрес.");
            System.exit(1);
        }
        loop(address, port);
    }

    private void loop(InetAddress address, int port) {
        boolean killClient = false;
        boolean welcomeShown = false;
        while(!killClient) {
            connectLoop(address, port);
            if(!welcomeShown) welcomeShown = showWelcome();
            killClient = mainLoop();
            println("Соединение разорвано.");
        }
    }

    private boolean showWelcome() {
        println("Вэлкам. Авторизируйтесь или зарегистрируйтесь, используя комманды:\n" +
                "Автор-ция: login username password\n" +
                "Рег-ция: register email username password");
        return true;
    }

    private void connectLoop(InetAddress address, int port){
        connected = false;
        while(!connected) {
            try {
                print("Попытка установить соединение с сервером.\r");
                con = new Connection(address, port);
                connected = con.checkConnection();
            } catch (IOException e) {
                handleException(e);
            }
            if(!connected) {
                clearLine();
                println("Не удалось установить соединение с сервером.");
                println("Попробовать снова? Y/y - да.");
                String in = getConsoleInput();
                if (in.equals("log")) {
                    handleCommand(new Command(LOG));
                    in = getConsoleInput();
                }
                if(in.trim().equals("Y") || in.trim().equals("y")) continue;
                else System.exit(1);
            } else {
                clearLine();
                println("Соединение установлено.");
            }

        }
    }

    private boolean mainLoop() {
        try {
            listen();
            connected = true;
            ConsoleInputHandler handler = new ConsoleInputHandler();
            while (connected) {
                //listener.listen();
                String input = getConsoleInput();
                if(onRegister) {
                    Command command = new Command(REGISTER);
                    command.setToken(token).setStrData(input);
                    con.send(command);
                    continue;
                }
                if(input.equals("log")) printLogs();
                if(handler.process(input)) {
                    Command command = handler.getCommand();
                    if(command == null) continue;
                    if(!authorised && handler.getExit()) return true;
                    if(!authorised && !handler.isAuthorise()) {
                        println("Вам необходимо авторизироваться.");
                        continue;
                    }
                    if(authorised && handler.isAuthorise()) {
                        println("Вы уже авторизированы.");
                        continue;
                    }
                    connected = handleCommand(command);
                    if(handler.getExit()) return true;
                }
            }
            return  false;
        } catch (Exception e) {
          handleException(e);
          return false;
        } finally {
            con.close();
            //listener.kill();
        }
    }

    private boolean handleCommand(Command c) {
        if (c.getType() == LOG) {
            printLogs();
            return true;
        }
        //PleaseWait waiter = new PleaseWait();
        try {
            if (c.getType() == IMPORT) prepareImport(c);
            if (c.getType() == INSERT) prepareInsert(c);
            c.setToken(token);
            con.send(c);
            //waiter.setStage(2);
            if(c.getType() == QUIT) return false;
            //Response response = con.receive(waiter);
            //log(response.getData());
            //if (response == null) throw new Exception("В ответ пришло null.");

            //responseString = processResponse(response, waiter);
        } catch(IOException e) {
            //waiter.stop();
            handleException(e);
          return false;
        } catch (Exception e) {
            //waiter.stop();
            handleException(e);
            return true;
            //return con.checkConnection();
        } //finally {
            //waiter.stop();
        //}
        //println(responseString);
        return true;
    }

    private void prepareInsert(Command com) throws Exception {
        String key = com.getStrData()[0];
        String json = com.getStrData()[1];
        String csv = MapWrapperUtils.parseJson(json);
        Malefactor mf = MapWrapperUtils.elementFromString(csv);
        com.setStrData(key);
        com.setData(mf);
    }

    private void prepareImport(Command com) throws Exception {
        String filePath = com.getStrData()[0];
        File file = openFile(filePath);
        if (file.length() > 65000)
            throw new Exception("Файл слишком большой. >64кБ");
        String fileStr = getFileString(file);
        com.setStrData(fileStr);
    }

    private String processResponse(Response res) throws Exception{
        if(res.isNotification()) return processNotif(res);
        StringBuilder message = new StringBuilder();
        if(res.getStatus() == WRONG_TOKEN) {
            authorised = false;
            token = null;
            return "Недействительный токен.";
        }
        if(res.getStatus() == EXPIRED_TOKEN) {
            authorised = false;
            token = null;
            return "Время действия токена истекло.";
        }
        if (res.getCommandType() == SHOW) {
            Map<String, Malefactor> map = (Map) res.getData();
            new MapValuesComparator(map, MapValuesComparator.Sorting.DEF); //does nothing
            if (map.size() == 0) return "Коллекция пуста.";
            else {
                map.entrySet().stream().map(i -> (i.getKey() + " : " + i.getValue()+ "\n")).forEach(message::append);
                if (!message.toString().equals("")) message.deleteCharAt(message.length() - 1);
                //return message.toString();
            }
        } else if(res.getCommandType() == LOGIN) {
            message.append(processLogin(res));
        }else if(res.getCommandType() == REGISTER) {
            //waiter.stop();
            message.append(processReg(res));
        } else message.append((String) res.getData());
        return message.toString();
    }

    private String processLogin(Response res) {
        String message = null;
        switch (res.getStatus()) {
            case OK:
                token = (String) res.getData();
                authorised = true;
                message = "Вы успешно авторизировались.";
                break;
            case ERROR:
                message = (String) res.getData();
                break;
        }
        return message;
    }

    private String processReg(Response res) throws Exception {
        String message = null;
        if (res.getStatus() != OK) return (String) res.getData();
        token = (String) res.getData();
        println("Вам на почту отправлено письмо, содержащее токен,\n" +
                "который нужно ввести далее. (Действителен в течении 1 минуты.)");
        for(int i = 0; i < 3; ++i) {
            onRegister = true;
            //String in = getConsoleInput();
            //Command command = new Command(REGISTER);
            //command.setToken(token).setStrData(in);
            //con.send(command, null);
            Response response = con.receive();
            if(response.isNotification()) {
                --i;
                continue;
            }
            if(response.getStatus() == OK) {
                authorised = true;
                message = "Вы успешно зарегистрированы.";
                break;
            } else if(response.getStatus() == EXPIRED_TOKEN) {
                message = "Время действия токена регистрации истекло.\n" +
                        "Попробуйте зарегистрироваться снова.";
                break;
            } else if(response.getStatus() == WRONG_TOKEN) {
                println("Неверный токен, попробуйте ещё. Осталось попыток: " + (3-i));
            } else {
                message = (String)response.getData();
                break;
            }
        }
        onRegister = false;
        return message;
    }

    private String processNotif(Response res) {
        if(res.getStatus() == EXPIRED_TOKEN) {
            authorised = false;
            token = null;
        }
        return (String)res.getData();
    }

    private void listen() {
        new Thread(() -> {
            try {
                while(connected) {
                    Response response = con.receive();
                    String res = processResponse(response);
                    println(res);
                }
            } catch(IOException e) {handleException(e);}
            catch (Exception e) {
                e.printStackTrace();
                println(e.getMessage());}
        }).start();
    }
}
