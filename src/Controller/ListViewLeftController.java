package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Model.Model_Block;
import Model.Model_File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.util.Callback;
import javafx.scene.control.ListCell;

public class ListViewLeftController implements Initializable {
	private FileIOController fileIOController;
	private MainFrameController mainFrameController;


	@FXML
	private ListView<Model_Block> listView_left;
	@FXML
	private ObservableList<Model_Block> listItems = FXCollections.observableArrayList();
	@FXML
	private Model_File file;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listView_left.setCellFactory(new Callback<ListView<Model_Block>, ListCell<Model_Block>>() {
            @Override
            public ListCell<Model_Block> call(ListView<Model_Block> p) {
            	
                ListCell<Model_Block> cell = new ListCell<Model_Block>() {
                
                    @Override
                    protected void updateItem(Model_Block t, boolean empty) {
                        super.updateItem(t, empty);
                        if (t != null) {
                        	int blankNum = t.getLeftBlank();
                        	StringBuilder sb = new StringBuilder();
                        	if(!t.isModified()){
	                        	for(int j = 0 ; j < t.getLeftLineInfo().size() ; j++){
	                        		sb.append(file.getLines().get((t.getLeftLineInfo().get(j))).getValue()+"\t\n");
	                        	}
	                        	for(int j = 0 ; j < blankNum ; j++){
	                        		sb.append("\t\n");
	                        	}
                        	}else if(t.isModified() && t.getFlag()==1){
                        		System.out.println("when t is modified as right to left");
	                        	for(int j = 0 ; j < t.getLeftLineInfo().size() ; j++){
		                        	sb.append(fileIOController.getRightFile().getLines().get((t.getLeftLineInfo().get(j))).getValue()+"\t\n");
		                       	}
                        	}else if(t.isModified() && t.getFlag()==2){
	                        	for(int j = 0 ; j < t.getLeftLineInfo().size() ; j++){
	                        		sb.append(file.getLines().get((t.getLeftLineInfo().get(j))).getValue()+"\n");
	                        	}
                        	}
                        	if(!t.isSame()){
                         		this.getStyleClass().add("diff-cell");
                         	}else{
                         		System.out.println("white");
                         	}
                        	this.setText(sb.toString());
                        }else{
                        	setText("");
                        }
                    }
                };
                cell.setEditable(true);
                return cell;
            }
        });
        listView_left.setItems(listItems);
        
	}
	
	public void showFile(){
		file = fileIOController.getLeftFile();
		listItems = fileIOController.getBlocks();
		Model_Block initBlock;
		if(listItems.isEmpty()){
			System.out.println("listItems are empty"+listItems.size());
			initBlock = new Model_Block(file, fileIOController.getRightFile());
			listItems.add(initBlock);
		}else{
			System.out.println("listItems are Not empty anyway"+listItems.size());
			initBlock = new Model_Block(file, fileIOController.getRightFile());
			listItems.set(0, initBlock);
			System.out.println(listItems.size());
		}
		listView_left.setItems(listItems);
		
		for(int i = 0 ; i < listItems.size(); i++){
			System.out.println(listItems.get(i).isSame());
		}
	}
	
	public void setControllerFileIO(FileIOController fileIOController){
		this.fileIOController = fileIOController;
	}
	public void setMainFrameController(MainFrameController mainFrameController){
		this.mainFrameController = mainFrameController;
	}
	public ListView<Model_Block> getListViewLeft(){
		return this.listView_left;
	}
	
	
	@FXML
	public void onListViewLeftMouseClicked(){
		int index = listView_left.focusModelProperty().getValue().getFocusedIndex();
		System.out.println("BlockIdx : " + index);
		fileIOController.setSelectedBlockIndex(index);
		
		if(!listView_left.getItems().get(index).isSame()){
			mainFrameController.setMerge("true");
		}
		else mainFrameController.setMerge("false");
	}
}
