package vikbur;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.hyperledger.fabric.gateway.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

public class Node extends Thread{

    private static List<String> events = new ArrayList<>();
    private boolean isActive;

    public Node(){
        super();
        isActive = true;
    }

    public void disable(){
        isActive = false;
    }

    @Override
    public void run() {
        //иммитируем постоянно работающий узел
        //System.out.println("Node started");

        while (isActive) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Node stopped");
                break;
            }
        }

    }

    //добавляем новые события
    public static void updateEvents(List<String> newEvents) throws IOException{

        Gateway.Builder builder = getBuilderForContract();

        // Create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("MyContract");

            for (String newEvent: newEvents){
                if (!events.contains(newEvent)){

                    contract.createTransaction("updateEvent").submit(newEvent);
                }
            }

        } catch (ContractException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static Gateway.Builder getBuilderForContract() throws IOException{

        // Load an existing wallet holding identities used to access the network.
        Path walletDirectory = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);

        // Path to a common connection profile describing the network.
        Path networkConfigFile = Paths.get("connection.json");

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "user1")
                .networkConfig(networkConfigFile);

        return builder;
    }
    //возвращаем текущие данные лога
    public static String getLog() throws IOException, ContractException {

//        StringBuilder sb = new StringBuilder();
//
//        for (String str: events) {
//            sb.append(str).append("\n");
//        }
//
//        return sb.toString();

        Gateway.Builder builder = getBuilderForContract();

        // Create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("MyContract");

            byte[] queryAllEvents = contract.evaluateTransaction("queryAllEvents");

            return new String(queryAllEvents, StandardCharsets.UTF_8);

        }

    }


}
