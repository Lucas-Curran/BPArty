package com.mygdx.game;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class CrashInfo implements Screen {	
	
	public static final Logger LOGGER = LogManager.getLogger(CrashInfo.class);
	
	 Exception error;
	
	 FileWriter fw;
	 PrintWriter pw;
	 Label textBox;
	 TextButton sendCrashBtn, defaultQuitBtn;
	 final BPArty game;
	 Stage stage;
	OrthographicCamera camera;
	 
		public CrashInfo(Exception error, BPArty game) throws IOException {
			
			this.error = error;
			this.game = game;
			
			fw = new FileWriter("CrashReport.txt", true);
			pw = new PrintWriter(fw);
			
			camera = new OrthographicCamera();
			camera.setToOrtho(false, BPArty.WIDTH/2, BPArty.HEIGHT/2);
			
		}
	
		
		public void writeCrash() throws IOException {
			
			//Writes the error to a file
			pw.print(new SimpleDateFormat("[yyyy.MM.dd.HH:mm:ss] ").format(new Date()));
			error.printStackTrace(pw);			
			
			pw.close();
			fw.close();

			sendMail(error);	
			
			LOGGER.info("Crash written successfully.");
			
		}
		
		//Email of crash log is sent to email
		private void sendMail(Exception report) {
			
			
			//Insecure email
			
			final String username = "bpagame54@gmail.com";
			final String password = "SuperPassword5";

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			report.printStackTrace(pw);
			String reportString = sw.toString();
			
			//Most of following code is from https://www.tutorialspoint.com/javamail_api/javamail_api_gmail_smtp_server.htm
			
			  Properties props = new Properties();
			  props.put("mail.smtp.auth", "true");
		      props.put("mail.smtp.starttls.enable", "true");
		      props.put("mail.smtp.host", "smtp.gmail.com");
		      props.put("mail.smtp.port", "587");
		      props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		      
		      Session session = Session.getInstance(props, new Authenticator() {
		            protected PasswordAuthentication getPasswordAuthentication() {
		                return new PasswordAuthentication(username, password);
		            }
		        });
		      
		      try {
		    	  Message message = new MimeMessage(session);
		    	  message.setFrom(new InternetAddress("bpagame54@gmail.com"));
		    	  message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("bpagame54@gmail.com"));
		    	  message.setSubject("[Crash Report]");
		    	  message.setText("Here is a reported crash... \n\n " + reportString);
		    	  
		    	  Transport.send(message);
		    	  
		      } catch (MessagingException e) {
		    	  throw new RuntimeException(e);
		      }
			
		}


		@Override
		public void show() {
			
			stage = new Stage();
			
			Gdx.input.setInputProcessor(stage);
			
			//Create crash report buttons, only show if a crash occurs
			sendCrashBtn = new TextButton("Yes", game.ButtonStyles("MenuRectangle", "MenuRectangle"));
			defaultQuitBtn = new TextButton("No", game.ButtonStyles("MenuRectangle", "MenuRectangle"));
			
			sendCrashBtn.getLabel().setAlignment(Align.left);
			sendCrashBtn.getLabelCell().padLeft(43);
			sendCrashBtn.getLabel().setFontScale(2,2);
		
			defaultQuitBtn.getLabel().setAlignment(Align.left);
			defaultQuitBtn.getLabelCell().padLeft(52);
			defaultQuitBtn.getLabel().setFontScale(2,2);
		
			sendCrashBtn.setPosition(350, 250);
			defaultQuitBtn.setPosition(650, 250);			
			
			stage.addActor(sendCrashBtn);
			stage.addActor(defaultQuitBtn);	
			
			textBox = new Label(String.format("%s", "A crash has occured! Would you like to report it?"), new Label.LabelStyle(new BitmapFont(), Color.WHITE));			
			
			stage.addActor(textBox);
			textBox.setPosition(410, 500);
		}


		@Override
		public void render(float delta) {
			
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			camera.update();
			game.batch.setProjectionMatrix(camera.combined);			
			
			if (sendCrashBtn.isPressed()) {
				try {
					writeCrash();
					Gdx.app.exit();
				} catch (IOException n) {
					//Do nothing
				}
			} else if (defaultQuitBtn.isPressed()) {
				Gdx.app.exit();
			}
			
			stage.act(delta);
			stage.draw();
		}


		@Override
		public void resize(int width, int height) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pause() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void resume() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void hide() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}
}
