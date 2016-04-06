package edu.nus.iss.SE24PT8.universityStore.gui.components.category;

import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.*;

import edu.nus.iss.SE24PT8.universityStore.domain.Category;
import edu.nus.iss.SE24PT8.universityStore.exception.BadCategoryException;
import edu.nus.iss.SE24PT8.universityStore.gui.common.BaseDialogBox;
import edu.nus.iss.SE24PT8.universityStore.gui.framework.SubjectManager;
import edu.nus.iss.SE24PT8.universityStore.gui.mainWindow.MainWindow;
import edu.nus.iss.SE24PT8.universityStore.main.Store;
import edu.nus.iss.SE24PT8.universityStore.util.Constants;
import edu.nus.iss.SE24PT8.universityStore.util.ReturnObject;
/**
*
* @author Mugunthan
*/
public class AddCategoryDialog extends BaseDialogBox {

	private static final long serialVersionUID = 1L;
	
    private JTextField nameField;
    private JTextField codeField;

    public AddCategoryDialog () {
        super (MainWindow.getInstance(), "Add Cetegory","add");
        super.setModalityType(Dialog.ModalityType.MODELESS);
    }

    protected JPanel createFormPanel  ()  {
    	JPanel p = new JPanel ();
        
        p.setLayout (new GridLayout (0, 2));
        p.add (new JLabel ("Code"));
        codeField = new JTextField (3);
        p.add (codeField);
        p.add(new JLabel ("Name"));
        nameField = new JTextField (20);
        p.add (nameField);
//    	EntryJPanel e = new EntryJPanel();
//    	
//        e.addCompCol1(new JLabel ("Code"));
//        e.addCompCol1(new JTextField (3));
//        
//        e.addCompCol1(new JLabel ("Name"));
//        e.addCompCol1( new JTextField (20));
        return p;
    }

    protected boolean performCreateUpdateAction () {
        String name = nameField.getText();
        String code = codeField.getText();
        if ((name.length() == 0) || (code.length() == 0)) {
        	JOptionPane.showMessageDialog(rootPane,
					Constants.CONST_CAT_ERR_INVALID_DETAILS,
					"Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (code.length() != 3) {
        	JOptionPane.showMessageDialog(rootPane,
					Constants.CONST_CAT_ERR_LONG_CODE,
					"Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
			Category  cat = Store.getInstance().getMgrCategory().addCategory(code, name);
        	JOptionPane.showMessageDialog(rootPane,
        			Constants.CONST_CAT_MSG_CREATION_SUCUESS,
					"Success", JOptionPane.INFORMATION_MESSAGE);
        	SubjectManager.getInstance().Update("CategoryPanel", "Category", "Add");
        	return true;
		} catch (BadCategoryException e) {
        	JOptionPane.showMessageDialog(rootPane,
        			e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
        	return false;
		}
    }

}