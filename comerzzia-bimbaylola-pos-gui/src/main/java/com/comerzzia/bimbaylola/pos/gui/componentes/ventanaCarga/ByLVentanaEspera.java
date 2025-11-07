package com.comerzzia.bimbaylola.pos.gui.componentes.ventanaCarga;

import org.apache.log4j.Logger;

import com.comerzzia.pos.core.gui.POSApplication;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ByLVentanaEspera extends Stage {

	private static Logger log = Logger.getLogger(ByLVentanaEspera.class);

	static ByLVentanaEspera ventana;

	protected Scene scene;

	protected BorderPane panelInterno;
	protected VBox vBoxMensage;
	protected static Label lbMensaje = new Label();
	protected static ImageView imageView = new ImageView();

	public static void crearVentanaCargando(Stage stageOwner) {
		ventana = new ByLVentanaEspera();
		ventana.initOwner(stageOwner);
		ventana.setResizable(false);

		/* Configuración de la ventana */
		ventana.centerOnScreen();
		ventana.initModality(Modality.WINDOW_MODAL);
		ventana.setIconified(false);
		ventana.panelInterno = new BorderPane();
		ventana.initStyle(StageStyle.TRANSPARENT); // Quitamos el marco de la ventana

		/* Imagen */
		Image imagen = POSApplication.getInstance().createImage("dialog/iconoInformacion.png");
		imageView.setImage(imagen);
		ventana.panelInterno.setLeft(imageView);
		BorderPane.setMargin(imageView, new Insets(10)); // probar con css

		/* Mensaje */
		ventana.vBoxMensage = new VBox();
		ventana.vBoxMensage.setAlignment(Pos.CENTER_LEFT);

		lbMensaje.setWrapText(true);
		lbMensaje.setMinWidth(180);
		lbMensaje.setMaxWidth(200);

		if (!Double.isNaN(POSApplication.getInstance().getStage().getHeight()) && Double.isNaN(POSApplication.getInstance().getStage().getWidth())) {
			lbMensaje.setMaxWidth(POSApplication.getInstance().getStage().getWidth() * 0.8);
			lbMensaje.setMaxHeight(POSApplication.getInstance().getStage().getHeight() * 0.8);
		}

		ventana.vBoxMensage.getChildren().add(lbMensaje);
		ventana.panelInterno.setCenter(ventana.vBoxMensage);
		BorderPane.setAlignment(ventana.vBoxMensage, Pos.CENTER);
		BorderPane.setMargin(ventana.vBoxMensage, new Insets(20, 20, 35, 2 * 10)); // probar con estilos

		ventana.setMaxWidth(300);
		ventana.scene = new Scene(ventana.panelInterno);
		ventana.setScene(ventana.scene);

		/* Estilos de la ventana */
		ventana.panelInterno.setId("ventanaDialogo");
		ventana.scene.setFill(Color.TRANSPARENT);
		POSApplication.getInstance().addBaseCSS(ventana.scene);
	}

	public static void mostrar() {
		log.debug("mostrar()");
		if (ventana != null) {
			ventana.show();
		}
	}

	public static void cerrar() {
		log.debug("cerrar()");
		if (ventana != null) {
			ventana.close();
		}
	}

	public static void setMensaje(String mensaje) {
		lbMensaje.setText(mensaje);
	}
}
