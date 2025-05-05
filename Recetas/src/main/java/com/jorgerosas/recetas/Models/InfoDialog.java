package com.jorgerosas.recetas.Models;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class InfoDialog {
    public static void showInfo(){
        Dialog<Void> aboutDialog = new Dialog<>();
        aboutDialog.setTitle("Información");
        aboutDialog.setHeaderText("Acerca de esta aplicación");

        // Create content for the dialog
        VBox content = new VBox();
        content.setSpacing(10);

        Text text1 = new Text("Recetario digital desarrollado para el Dr. EDUARDO BARRAGÁN CURIEL");
        text1.setStyle("-fx-font-weight: bold;"); // Keeps default font family/size

        content.getChildren().addAll(
                text1,
                new Text("Version: 1.2.6"),
                new Text(""),
                new Text("Guía rápida:"),
                new Text("-En 'Nueva Receta' puede generar una receta introduciendo el nombre y la descripción que esta va a tener,"),
                new Text("  el apartado 'fecha' carga la fecha de la computadora en automático, no es necesario introducirla, a no "),
                new Text("  ser que el usuario así lo quiera. "),
                new Text("-Al presionar 'Generar PDF', la receta se guardará y se generará en forma de PDF (esto puede tardar unos segundos)"),
                new Text("  con la plantilla de la Unidad de Cancerología. "),
                new Text("-Para ver la carpeta con todos los PDF's presione el botón 'Ver Recetas'."),
                new Text("-En 'Cargar Receta' puede cargar los datos de recetas previas para reutilizar la descripción de la misma,"),
                new Text("  puede buscar recetas previas por nombre o fecha en el apartado de 'buscar', para cargar o eliminar recetas,"),
                new Text("-Al eliminar una receta NO borrará el PDF, solo no estará disponible para su reutilización."),
                new Text("-No hay límite de texto para el campo Descripción, solo para el campo Nombre"),
                new Text("-Para guardar el archivo PDF de forma segura, el programa remplaza por un guión bajo _ caracteres como los"),
                new Text(" siguientes: “ ” ‘ ’ ( ) [ ] { } ‹ › + − × ÷ = ≠ ± < > $, €, ¥, ₹, ₿ , ; :"),
                new Text(" estos caracteres aún se ven reflejados en el contenido del PDF, solo se remplazan en el nombre del archivo."),
                new Text(""),
                new Text("Desarrollado por: Ing. Jorge S. Rosas Gómez - Información de contacto: jorge.99rg@gmail.com")
        );

        aboutDialog.getDialogPane().setContent(content);
        aboutDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        aboutDialog.showAndWait();
    }
}
