package controller;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import model.JDBCUtil;
import model.data;

public class LoginController {

    @FXML
    private TextField lo_email;

    @FXML
    private PasswordField lo_password;

    @FXML
    private Hyperlink lo_register_link;

    @FXML
    private Button lo_signin;

    @FXML
    private AnchorPane lo_view;

    @FXML
    private ImageView re_avatar;

    @FXML
    private TextField re_email;

    @FXML
    private Hyperlink re_login_link;

    @FXML
    private TextField re_name;

    @FXML
    private PasswordField re_password;

    @FXML
    private Button re_setavatar_btn;

    @FXML
    private Button re_signup;

    @FXML
    private AnchorPane re_view;
    
    @FXML
    private AnchorPane login_form;
    
    private Alert alert;
    
    private Connection connect;
	private PreparedStatement prepare;
	private ResultSet result;
	
	
public void loginBtn() throws Exception{

		
		//check the email or password feild do write yet? if not, give notification error!

			if (lo_email.getText().isEmpty() || lo_password.getText().isEmpty()) {
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Please enter email/password!");
				alert.showAndWait();
			} 
			else {
				String selectData = "SELECT name, email, password FROM user WHERE email = ? and password = ?";

				connect = JDBCUtil.getConnection();

				
					prepare = connect.prepareStatement(selectData);
					prepare.setString(1, lo_email.getText());
					prepare.setString(2, encodePassword(lo_password.getText()));

					result = prepare.executeQuery();

					// Nếu đăng nhập thành công sẽ chuyển sang giao diện chính của chương trình
					if (result.next()) {
						
						data.name = result.getString("name");
						
						alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information Message");
						alert.setHeaderText(null);
						alert.setContentText("Successfully Login!");
						alert.showAndWait();
						
						
						//Kết nối với giao diện chính khi đăng nhập thành công.
						Parent root = FXMLLoader.load(getClass().getResource("/view/home.fxml"));
						
						Stage stage = new Stage();
						Scene scene = new Scene(root);
						
						
						//Image image = new Image(getClass().getResourceAsStream("/view/images/cafe.png"));
						//stage.getIcons().add(image);
						stage.setTitle("ChatATC+ Application");
						
						stage.setScene(scene);
						stage.show();
						
						lo_signin.getScene().getWindow().hide();

					} else {
						// Nếu không sẽ xuất hiện thông báo
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Message");
						alert.setHeaderText(null);
						alert.setContentText("Incorrect Username/Password !");
						alert.showAndWait();

					}
				} 
	}

	
	public static String encodePassword(String password)
	{
		try {
			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encodeHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder hexString = new StringBuilder();
			for(byte b : encodeHash) {
				String hex = String.format("%02x", b);
				hexString.append(hex);
			}
			
			return hexString.toString();
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public void setAvatar() {

		FileChooser openFileChooser = new FileChooser();
		openFileChooser.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg", "*jpeg"));

		File file = openFileChooser.showOpenDialog(login_form.getScene().getWindow());

		if (file != null) {
			
			data.path = file.getAbsolutePath();

			re_avatar.setImage(new Image(file.toURI().toString(), 50, 50, false, true));
		}
	}
    
    public void reBtn() throws Exception{

			if (re_email.getText().isEmpty() || re_password.getText().isEmpty() || re_name.getText().isEmpty()|| data.path==null) {

				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Please fill all blank fields!");
				alert.showAndWait();

			} else {
				String reData = "INSERT INTO user(name, email, password, avatar)"
						+ "VALUES(?,?,?,?)";
				connect = JDBCUtil.getConnection();


					// Kiểm tra nếu email đã tồn tài.
					String checkEmail = "SELECT email FROM user WHERE email = '" + re_email.getText() + "'";

					prepare = connect.prepareStatement(checkEmail);
					result = prepare.executeQuery();
					
					
					String email_patern = "^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$";

					if (result.next()) {
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Message");
						alert.setHeaderText(null);
						alert.setContentText(re_email.getText() + " is already taken");
						alert.showAndWait();
					}

					// Kiểm tra độ dài kí tự mật khâu đăng kí
					else if (re_password.getText().length() < 4) {
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Message");
						alert.setHeaderText(null);
						alert.setContentText("The password must be at least 5 characters long!");
						alert.showAndWait();
					}
					
					
					else if(Pattern.matches(email_patern, re_email.getText())== false)
					{
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Message");
						alert.setHeaderText(null);
						alert.setContentText("The email INVALID: example@gmail.com");
						alert.showAndWait();
					}
					else {
						prepare = connect.prepareStatement(reData);
						
						prepare.setString(1, re_name.getText());
						prepare.setString(2, re_email.getText());
						prepare.setString(3, encodePassword(re_password.getText()));
						
						String path = data.path;
						path = path.replace("\\", "\\\\");

						prepare.setString(4, path);

						prepare.executeUpdate();
											
						alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information Message");
						alert.setHeaderText(null);
						alert.setContentText("Successfully registered Account!");
						alert.showAndWait();

						re_email.setText("");
						re_password.setText("");
						re_name.setText("");
						
					}

			}
		}
    
  public void switchForm(ActionEvent event) {
	  
	  if(event.getSource() == lo_register_link) {
		  lo_view.setVisible(false);
		  re_view.setVisible(true);
	  }
	  else if(event.getSource() == re_login_link) 
	  {
		  re_view.setVisible(false);
		  lo_view.setVisible(true);
		  
	  }
	
  }
}


