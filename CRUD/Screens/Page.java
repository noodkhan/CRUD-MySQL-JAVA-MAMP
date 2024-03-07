package Screens;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import Services.DataBaseQuery;
import java.util.ArrayList;


public class Page {
    // Env Setting Screens Width | Height
    JFrame frame;
    GraphicsDevice graphics = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice() ; 
    int width = graphics.getDisplayMode().getWidth() ; 
    int height = graphics.getDisplayMode().getHeight() ;     

    public void createBuffer(JTable table , String[][] data){
        // getData From table
        TableModel model = table.getModel();
        int rowSize = data.length; 
        int columnSize = data[0].length;  
        // Data from table + Create Buffer 
        for(int i = 0 ; i < rowSize ; ++i){
            for(int j = 0 ; j < columnSize ; ++j){
                Object cell = model.getValueAt(i , j); // cell
                System.out.println(" Value = " + cell + " Replace with " + data[i][j]);
                Object newCell = (Object) data[i][j] ; 
                model.setValueAt(newCell, i, j);
            }
        } 
    }

    public void clearBuffer(JTable table){
        // getData From table
        TableModel model = table.getModel();
        int rowSize = model.getRowCount() ; 
        int columnSize = model.getColumnCount();
        // Data from table + Clear Buffer
        for(int i = 0 ; i < rowSize ; ++i){
            for(int j = 0 ; j < columnSize ; ++j){
                Object cell = model.getValueAt(i , j); // cell
                System.out.println("ClearValue = " + cell);
                model.setValueAt(null, i, j);
            }
        } 
    }

    // Create Data for -> table
    public String[][] get(){
        ArrayList<String[]> response = new DataBaseQuery().display();
        int size = response.size();
        String[][] data = new String[size][];
        for(int i = 0 ; i < size ; ++i){
            String[] array = new String[2];
            array[0] = response.get(i)[0] ; // id
            array[1] = response.get(i)[1] ; // name
            data[i] = array ; 
        }
        return data;
    }

    public Page(){
        // Create JFrame
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("CRUD System MySQL");

        // Create JTable
        String[][] data = get() ; 
        String[] heading = {"id" , "name"} ; 
        JTable table = new JTable(data , heading);
        JScrollPane panel = new JScrollPane(table);
        frame.add(panel , BorderLayout.WEST) ; 

        // GetData Function + Buffer -> Container
        JTabbedPane container = new JTabbedPane();
        JPanel getDataContainer = new JPanel();
        JLabel textData = new JLabel("Data");
        JButton buttonData = new JButton("GetData");
        buttonData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[][] data = get() ; 
                clearBuffer(table) ; 
                createBuffer(table , data) ; 
            } 
        });
        getDataContainer.add(textData) ;  
        getDataContainer.add(buttonData); 

        // Read Function to -> Container
        JPanel readContainer = new JPanel();
        JLabel textRead = new JLabel("Enter ID");
        JTextField input_id_read = new JTextField("Insert ID \t\t");
        JButton buttonRead = new JButton("Search") ; 
        buttonRead.addActionListener(new ActionListener() {
        ArrayList<JLabel> bufferText = new ArrayList<>(); 
            @Override
            public void actionPerformed(ActionEvent e) {  
                JLabel outputText = new JLabel();
                String input = input_id_read.getText();
                int id = Integer.parseInt(input) ; 
                System.out.println(input);
                String str = "" ;  
                String response = new DataBaseQuery().read(id) ; 
                str += response ;
                outputText.setText(str) ; 
                outputText.setFont(new Font("Arial", Font.BOLD , 16));
                outputText.setForeground(Color.RED);
                bufferText.add(outputText) ; 
                for(int i = 0 ; i < bufferText.size() ; ++i){
                    readContainer.add(bufferText.get(i)) ; 
                }
            }
        });
        readContainer.add(textRead) ; 
        readContainer.add(input_id_read) ; 
        readContainer.add(buttonRead) ; 

        // Create Function to -> Container
        JPanel createContainer = new JPanel(); 
        JLabel textCreate = new JLabel("Enter Name");
        JTextField input_name_create = new JTextField(" Insert Name \t\t");
        JButton buttonCreate = new JButton("Create") ; 
        buttonCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = input_name_create.getText() ;                 
                new DataBaseQuery().create(input);
            }
        });
        createContainer.add(textCreate) ; 
        createContainer.add(input_name_create) ;  
        createContainer.add(buttonCreate) ; 

        // Delete Function to -> Container
        JLabel textDelete = new JLabel("Enter ID");
        JPanel deleteContainer = new JPanel();
        JTextField input_id_delete = new JTextField(" Insert ID \t\t") ;
        JButton buttonDelete = new JButton("Delete") ; 
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = input_id_delete.getText();
                int id = Integer.parseInt(input) ; 
                new DataBaseQuery().delete(id); 
            }
        });
        deleteContainer.add(textDelete);  
        deleteContainer.add(input_id_delete) ; 
        deleteContainer.add(buttonDelete)  ; 

        // Update Function to -> Container
        JPanel updateContainer = new JPanel();
        JLabel textUpdate = new JLabel("Enter ID & Name");
        JTextField input_id_update = new JTextField("Insert ID \t");
        JTextField input_name_update = new JTextField("Insert name \t") ;  
        JButton buttonUpdate = new JButton("Update") ; 
        buttonUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputID = input_id_update.getText();
                String name = input_name_update.getText(); 
                int id = Integer.parseInt(inputID) ; 
                new DataBaseQuery().update(id, name);
            }
        });
        updateContainer.add(textUpdate) ; 
        updateContainer.add(input_id_update) ;
        updateContainer.add(input_name_update) ;  
        updateContainer.add(buttonUpdate) ; 

        // build Container For Multiple Pages
        container.add("DATA" ,  getDataContainer);
        container.add("READ" ,     readContainer); 
        container.add("CREATE" , createContainer); 
        container.add("DELETE" , deleteContainer); 
        container.add("UPDATE" , updateContainer); 

        // add Container to Frame
        frame.add(container , BorderLayout.CENTER) ; 
 
        // Rest of the frame Setting 
        frame.setSize(width / 2, height / 2);
        frame.setVisible(true); 
   }
}
