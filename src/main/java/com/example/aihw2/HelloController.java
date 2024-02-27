package com.example.aihw2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class HelloController  implements Initializable {
    HashMap<String, Integer> colorMap = new HashMap<>();



    double[] targetOutputOrange = {1.0, 0.0, 0.0}; //  encoding for orange
    double[] targetOutputBanana = {0.0, 1.0, 0.0}; // encoding for banana
    double[] targetOutputApple = {0.0, 0.0, 1.0}; //  encoding for apple










    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        colorMap.put("Red", 1);
        colorMap.put("Yellow", 2);
        colorMap.put("Orange", 3);
        ACtiv.getItems().addAll("sigmoid", "tanh","Relu");

        // Set default value if needed
        ACtiv.setValue("sigmoid");
    }

    @FXML
    private TextField Hidden;

    @FXML
    private Button Select_file;

    @FXML
    private Button Trin;

    @FXML
    private Button Clear;

    @FXML
    private Button v1;

    @FXML
    private Button v2;


    @FXML
    private TextField ep;

    @FXML
    private ComboBox<String> ACtiv = new ComboBox<>();
    @FXML
    private Button Test;
    @FXML
    private TextField LeRate;

    @FXML
    private TextField Goal;
    @FXML
    private TextField path;
    @FXML
    private TextArea view;
    Network network ;
    File selectedFile ;
    int line_cont = 0 ;

    @FXML
    void onHelloButtonClick(ActionEvent event) throws IOException {
        if (event.getSource()==Select_file){

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Test Data File");
             selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                path.setText(selectedFile.getPath());
            }

        }
        if (event.getSource() == Clear){
            view.clear();
        }

        if (event.getSource()==Trin){


             network = new Network(2, Integer.parseInt(Hidden.getText()), 3,ACtiv.getValue(),Double.parseDouble(LeRate.getText())); // 3 neurons in the output layer

            Neuron[] inputNeurons = network.getInputLayer().getNeurons();

        line_cont =0 ;

            double counter=0.0;
            try {
 // epch
                for(int j=0;j< Integer.parseInt(ep.getText())   ;j++ ) {



                    BufferedReader reader = new BufferedReader(new FileReader(selectedFile.getPath()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line_cont ++;

                        String[] parts = line.split(",");
                        String fruitType = parts[0]; // name
                        double sweetnessValue = Double.parseDouble(parts[1]); // swett
                        int colorValue = colorMap.get(parts[2]); // color

                        inputNeurons[0].setColor(colorValue);
                        inputNeurons[1].setSweetness(sweetnessValue);

                        double[] targetOutput;
                        switch (fruitType) {
                            case "Orange":
                                targetOutput = targetOutputOrange;
                                break;
                            case "Banana":
                                targetOutput = targetOutputBanana;
                                break;
                            case "Apple":
                                targetOutput = targetOutputApple;
                                break;
                            default:
                                targetOutput = new double[]{0.0, 0.0, 0.0};
                        }

                        Neuron[] outputNeurons = network.getOutputLayer().getNeurons();
                        for (int i = 0; i < outputNeurons.length; i++) {
                            outputNeurons[i].setTargetOutput(targetOutput[i]); //yd
                        }

                        network.calculateHiddenLayerOutput();

                        network.calculateOutputLayerOutput();

                        network.calculateOutputLayerError();


// Get the output
                        Neuron[] outputNeuron = network.getOutputLayer().getNeurons();
                        double[] networkOutput = new double[outputNeurons.length];
                        for (int t = 0; t < outputNeurons.length; t++) {
                            networkOutput[t] = outputNeurons[t].getOutput();
                        }



                        int maxIndex = 0;
                        for (int f = 1; f < networkOutput.length; f++) {
                            if (networkOutput[f] > networkOutput[maxIndex]) {
                                maxIndex = f;
                            }
                        }

                        String fruitName;
                        switch (maxIndex) {
                            case 0:
                                fruitName = "Orange";
                                break;
                            case 1:
                                fruitName = "Banana";
                                break;
                            case 2:
                                fruitName = "Apple";
                                break;
                            default:
                                fruitName = "Unknown";
                        }
                        if(fruitType.equals(fruitName)){
                            counter ++;
                        }

                      //  view.appendText("\n the input: "+fruitType+" The network predicts: " + fruitName+"\n");

                        network.trainNetwork();
                        if ((network.getMSE()/line_cont ) <= Double.parseDouble(Goal.getText()) ){

                            break;

                        }
                    }


                    reader.close();

                    if ((network.getMSE()/line_cont ) <= Double.parseDouble(Goal.getText()) ){
                        view.appendText("ap = "+j+"\n");
                        view.appendText("line:"+line_cont+"\n");

                        break;

                    }
                }

            }

            catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            view.appendText("\n the true data is: "+counter+"\n");
            view.appendText("\n MSE: "+network.getMSE()/line_cont+"\n");

            view.appendText("\nthe acuracy is: "+(counter/line_cont)*100+"%\n");





    }

        if (event.getSource() == Test){

            Neuron[] inputNeurons = network.getInputLayer().getNeurons();
int linee = 0 ;
            double counter=0.0;
            try {



                    BufferedReader reader = new BufferedReader(new FileReader(selectedFile.getPath()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        String fruitType = parts[0];
                        double sweetnessValue = Double.parseDouble(parts[1]);
                        int colorValue = colorMap.get(parts[2]);
                        linee ++ ;
                        inputNeurons[0].setColor(colorValue);
                        inputNeurons[1].setSweetness(sweetnessValue);

                        double[] targetOutput;
                        switch (fruitType) {
                            case "Orange":
                                targetOutput = targetOutputOrange;
                                break;
                            case "Banana":
                                targetOutput = targetOutputBanana;
                                break;
                            case "Apple":
                                targetOutput = targetOutputApple;
                                break;
                            default:
                                targetOutput = new double[]{0.0, 0.0, 0.0};
                        }

                        Neuron[] outputNeurons = network.getOutputLayer().getNeurons();


                        network.calculateHiddenLayerOutput();

                        network.calculateOutputLayerOutput();



// Get the output
                        Neuron[] outputNeuron = network.getOutputLayer().getNeurons();
                        double[] networkOutput = new double[outputNeurons.length];
                        for (int t = 0; t < outputNeurons.length; t++) {
                            networkOutput[t] = outputNeurons[t].getOutput();
                        }

                        int maxIndex = 0;
                        for (int f = 1; f < networkOutput.length; f++) {
                            if (networkOutput[f] > networkOutput[maxIndex]) {
                                maxIndex = f;
                            }
                        }

                        String fruitName;
                        switch (maxIndex) {
                            case 0:
                                fruitName = "Orange";
                                break;
                            case 1:
                                fruitName = "Banana";
                                break;
                            case 2:
                                fruitName = "Apple";
                                break;
                            default:
                                fruitName = "Unknown";
                        }
                        if(fruitType.equals(fruitName)){
                            counter ++;
                        }

                          view.appendText("\n the input: "+fruitType+" The network predicts: " + fruitName+"\n");


                    }
                    reader.close();


            }

            catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            view.appendText("\n the true data is: "+counter+"\n");
            view.appendText("\n linee: "+linee+"\n");

            view.appendText("\nthe acuracy Test is: "+((counter/(linee))*100+"%\n"));








        }



    }
}