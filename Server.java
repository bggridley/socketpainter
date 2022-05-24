import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Server {

    private Vector<Pair> clients;
    private Vector<Shape> shapes;
    private ServerSocket ss;

    private class Pair {

        public Socket socket;
        public String name;

        public Pair(Socket s, String name) {
            this.socket = s;
            this.name = name;
        }
    }

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        clients = new Vector<>();
        shapes = new Vector<Shape>(); // shapes will be preserved, but chat messages will not.

        try {
            ss = new ServerSocket(7000);

            AwaitDisconnect ad = new AwaitDisconnect();
            Thread adt = new Thread(ad);
            adt.start();

            AwaitClient ac = new AwaitClient();
            Thread act = new Thread(ac);
            act.start();
            System.out.println("Waiting for clients...");

            act.join(); // blocking

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AwaitDisconnect implements Runnable {

        public void run() {

        }
    }

    private class AwaitClient implements Runnable {

        public void run() {
            try {
                while (true) {
                    Socket s = ss.accept();

                    ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                    String name = (String) ois.readObject();
                    Pair p = new Pair(s, name);
                    clients.add(p);

                    sendMessage(name + " connected.");

                    for (Shape shape : shapes) {
                        sendShape(null, shape, p.socket);
                    }
                    sendRepaint(s);

                    ClientConnection connection = new ClientConnection(s);
                    Thread connectionThread = new Thread(connection);
                    connectionThread.start();

                    System.out.println("Added client: " + name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void updateOnlineCount() {
        for (Pair p : clients) {
            Socket s = p.socket;
            try {
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

                oos.writeObject("count");
                oos.writeObject(clients.size());

                // oos.close();

            } catch (Exception e) {
                System.out.println("Expecting a disconnect from client soon. Object stream failed to open.");
            }
        }
    }

    public void sendMessage(String message) {
        for (Pair p : clients) {
            Socket s = p.socket;

            try {
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

                oos.writeObject("message");
                oos.writeObject(message + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendRepaint(Socket recipient) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(recipient.getOutputStream());

            oos.writeObject("repaint");

        } catch (Exception e) {

        }
    }

    public void sendShape(Socket sender, Shape shape, Socket recipient) {

        if (recipient == null) {
            for (Pair p : clients) {
                Socket s = p.socket;
                try {
                    if (s != sender) {
                        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

                        oos.writeObject("shape");
                        oos.writeObject(shape);

                        // oos.close();
                    }
                } catch (Exception e) {

                }

                sendRepaint(s);
            }
        } else {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(recipient.getOutputStream());

                oos.writeObject("shape");
                oos.writeObject(shape);
            } catch (Exception e) {

            }
        }
    }

    private class ClientConnection implements Runnable {

        private Socket s;

        public ClientConnection(Socket socket) {
            this.s = socket;
        }

        public void run() {
            updateOnlineCount();
            try {
                while (true) {
                    // read stuff

                    ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                    String header = (String) ois.readObject();

                    if (header.equals("shape")) {
                        Shape shape = (Shape) ois.readObject();
                        shapes.add(shape);

                        // System.out.println("received a shape;");
                        sendShape(s, shape, null);

                        shapes.add(shape);
                        // oos.writeObject(shape);
                    } else if (header.equals("message")) {
                        sendMessage((String) ois.readObject());
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
                Iterator<Pair> it = clients.iterator();

                while (it.hasNext()) {
                    Pair p = it.next();
                    if (p.socket == s) {
                        it.remove();
                        sendMessage(p.name + " disconnected.");
                        System.out.println(p.name + " disconnected");
                        break;
                    }
                }
               
                updateOnlineCount();

                // s.close();
            }
        }
    }
}
