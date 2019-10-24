package application.helper;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;

public class BindingsFactory {
	
	/**
	 * Binds the specified text field and button so that button is only enabled when the text field
	 * is not empty
	 */
	public void bindFieldButton(TextField field, Button button) {
		BooleanBinding bb = new BooleanBinding() {
			{
				super.bind(field.textProperty());
			}
			@Override
			protected boolean computeValue() {
				return (field.getText().isEmpty() || field.getText().trim().length() == 0);
			}			
		};
		button.disableProperty().bind(bb);
	}
	
	/**
	 * Binds the specified text area and button so that button is only enabled when the text area selected text
	 * is not empty, and the audioname text field is also not empty
	 */
	public void bindTextAreaButton(TextField field, TextArea area, Button button) {
		BooleanBinding bb = new BooleanBinding() {
			{
				super.bind(field.textProperty(), area.selectedTextProperty());
			}
			@Override
			protected boolean computeValue() {
				return (area.getSelectedText().isEmpty() || area.getSelectedText().trim().length() == 0 || 
						field.getText().isEmpty() || field.getText().trim().length() == 0);
			}			
		};
		button.disableProperty().bind(bb);
	}
	
}
