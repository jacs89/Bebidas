package javafxbebidas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


/**
 * Clase principal para la gestión de bebidas. 
 * @author Jose Cabello
 */
public class JavaFXBebidas extends Application {
    
    private TextField campoNombre ;
    private TextField campoColor ;
    private TextField campoUnidades ;
    
    // Declarar arraylist para almacenar los inmuebles
    private static ArrayList <Bebida> listaBeb = new ArrayList() ;
    
    @Override
    public void start(Stage primaryStage) {
        // Medidas del listView
        final int ANCHO_LIST = 225 ;
        final int ALTO_LIST = 120 ;
        
        // Crear etiqueta
        Label etiNombre = new Label("Nombre:");
        // Crear campos de texto
        campoNombre = new TextField();
        Label etiColor = new Label("Color:");
        campoColor = new TextField();
        Label etiUni = new Label("Unidades:");
        campoUnidades = new TextField("1");
        campoUnidades.setPrefWidth(50);
        Button btnAceptar = new Button("Añadir a la lista") ;
        Button btnBorrar = new Button("Borrar de la lista") ;
        Button btnGuardar = new Button("Guardar a disco") ;
        
        // Crear el listView
        ListView listView = new ListView();

        // Cargar la lista desde disco
        listaBeb = cargarLista() ;
        
        if (listaBeb.isEmpty()) {
            // Mostrar mensaje
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Aviso.");
            alert.setContentText("No se cargaron datos previos en la aplicación.\n Fichero no encontrado o vacío.");
            alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
        }
        
        // Añadir los elementos recién cargados en la lista al listView                    
        for (Bebida bebida : listaBeb) {
            listView.getItems().add(bebida.toString());
        }
            
        // Medidas del listView
        listView.setPrefSize(ANCHO_LIST, ALTO_LIST);
        
        
        // Cuando se haga click en el botón de añadir, añadir a la lista la bebida
        btnAceptar.setOnAction(event -> {           
            // Añadir a la lista una bebida
            altaBebida(listView) ;     
        });
        
        // Cuando se haga click en el botón de borrar, eliminar la bebida del
        // listview y de la lista de bebidas.
        btnBorrar.setOnAction(event -> {
            //Borrar de la lista una bebida
            borrarBebida(listView);            
        });
        
        // Al hacer click en el botón de guardar
        btnGuardar.setOnAction(event -> {
            if (guardarArrayEnFichero(listaBeb)) {
                // Mostrar mensaje
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Información.");
                alert.setContentText("Datos guardados correctamente.") ;
                alert.setHeaderText(null);
                Optional<ButtonType> result = alert.showAndWait() ; 
            }
                
        });
            

                      
        // Seleccionar el primer elemento del modelo del ListView
        listView.getSelectionModel().selectFirst();
        
        // Crear panel
        FlowPane fpane = new FlowPane();
        
        // Espacio entre filas y columnas
        fpane.setHgap(5);
        fpane.setVgap(5);
        
        // Establecer la distancia desde el borde del panel a los controles
        fpane.setPadding(new Insets(10));
      
        // Cargar una hoja de estilo específica
        fpane.getStylesheets().add("recursos/css/estilo.css");
        
        // Añadir los componentes al panel.
        fpane.getChildren().add(etiNombre);
        fpane.getChildren().add(campoNombre);
        fpane.getChildren().add(etiColor);
        fpane.getChildren().add(campoColor);
        fpane.getChildren().add(etiUni);
        fpane.getChildren().add(campoUnidades);
        fpane.getChildren().add(listView);
        fpane.getChildren().add(btnAceptar);
        fpane.getChildren().add(btnBorrar);
        fpane.getChildren().add(btnGuardar);
        
        // Añadir el panel a la escena
        Scene scene = new Scene(fpane, 580, 300);
        
                
        primaryStage.setTitle("Bebidas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Crea el objeto bebida con los datos introducidos en los controles de
     * la ventana.
     * @return El objeto bebida o null si hubo algún problema de validación con
     *         el dato de las unidades.
     */
    private Bebida crearBebida() {
        Bebida elemento = null ;
        String nombre = campoNombre.getText().trim() ;
        String color = campoColor.getText().trim() ;
        int unidades = 0 ;
        try {
            // Pasar a entero el texto introducido en el campo de unidades
            unidades = Integer.parseInt(campoUnidades.getText().trim()) ;
            // Comprobar si están todos los datos rellenos
            if (!(nombre.isEmpty() || color.isEmpty() ||
                campoUnidades.getText().trim().isEmpty()))  {
                // Crear el objeto bebida
                elemento = new Bebida(nombre, color, unidades) ;
            }
        } catch (NumberFormatException e) {
            // Mostrar mensaje
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error.");
            alert.setContentText("Se espera un número entero para las unidades.");
            alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
        }
               
        return elemento ;
    }
    
    
    private void altaBebida(ListView listView) {
        // Validar el número
            try {
                int unidades = Integer.parseInt(campoUnidades.getText().trim()) ;
                if (estaEnlaLista(listaBeb, campoNombre.getText().trim(), campoColor.getText().trim())) {
                    
                    // Mostrar mensaje
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Error.");
                    alert.setContentText("Ese nombre ya existe.");
                    alert.setHeaderText(null);
                    Optional<ButtonType> result = alert.showAndWait(); 
                    
                } else {
                    // Si se rellenaron los datos creo el objeto
                    Bebida objeto = crearBebida() ;
                    // Si se creó el objeto lo añadimos 
                    if (objeto == null) {
                        // Mostrar mensaje
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Error.");
                        alert.setContentText("Todos los datos deben estar rellenos.");
                        alert.setHeaderText(null);
                        Optional<ButtonType> result = alert.showAndWait();
                    } else {
                        // Añadir la cadena con los datos del objeto al listview
                        listView.getItems().add(objeto.toString());
                        // Añadir el objeto al ArrayList
                        listaBeb.add(objeto) ;
                    }
                }
            
            } catch (NumberFormatException e) {
                // Mostrar mensaje
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Error.");
                alert.setContentText("Se espera un número entero para las unidades.");
                alert.setHeaderText(null);
                Optional<ButtonType> result = alert.showAndWait();
            }
    }
    
    /**
     * Borra la bebida seleccionada de la lista (ArrayList) y de la vista (ListView)
     * @param listView 
     */
    private void borrarBebida(ListView listView) {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            listView.getItems().remove(selectedIndex);
            listaBeb.remove(selectedIndex);
        }
    }
    
    /**
     * Almacena el arraylist de inmuebles en el disco.
     * @param lista Arraylist a guardar en disco
     * @return true si se guarda el fichero sin error
     */
    public static boolean guardarArrayEnFichero(ArrayList<Bebida> lista) {
        boolean almacenado = false ; // Con esta variable devolverá true si todo
          //ha ido bien, y false en caso contrario.

        try ( FileOutputStream fichero = new FileOutputStream(new File("bebidas.dat"));
              ObjectOutputStream ficheroSalida = new ObjectOutputStream(fichero); ) {

            // Guardar la lista de objetos
            ficheroSalida.writeObject(lista) ;

            // Indicamos que lo que hayacambiado se ha guardado
            almacenado = true;
                        
        } catch (FileNotFoundException fnfe) {
            System.err.println("Error: El fichero no existe." + fnfe.getMessage());     
        } catch (IOException ioe) {
            System.err.println("Falló la grabación de datos: "  + ioe.getMessage());
        }
        return almacenado ;
    }
    
    
    /**
     * Lee la información que contiene el archivo utilizando la clase
     * FileInputStream. Con el método readObject se carga la información en la
     * lista.
     * 
     * @return  El arraylist relleno con los inmuebles o vacío si no hay datos en 
     * el fichero.
     */
    public static ArrayList<Bebida> cargarLista() {

        ArrayList<Bebida> listaDeBebidas = new ArrayList();
        
        try (FileInputStream fichero = new FileInputStream(new File("bebidas.dat")); 
             ObjectInputStream ficheroEntrada = new ObjectInputStream(fichero)) {

            //Carga de datos.
            listaDeBebidas = (ArrayList<Bebida>) ficheroEntrada.readObject();

        } catch (ClassNotFoundException cnfe) {
            System.err.println("No se pudo acceder a la clase "
                    + "adecuada para revertir la Serialización al leer "
                    + "del fichero." + cnfe.getMessage());

        } catch (FileNotFoundException fnfe) {
            System.err.println("Error: El fichero no existe."
                    + fnfe.getMessage());
        } catch (IOException ioe) {
            System.err.println("Error de Entrada/Salida: "
                    + "falló la lectura del fichero. La aplicación sigue "
                    + "funcionando sin haber cargado los datos del archivo,"
                    + " para permitir crearlo de nuevo." + ioe.getMessage());
        }
        return listaDeBebidas;
    }
    
    
    
    /**
     * Comprobar si en la lista de objetos bebida hay un elemento cuyo
     * nombre y color coincida con los parámetros enviados. 
     * Se empleará para evitar bebidas con el mismo nombre y color.
     * 
     * @param lista
     * @param nombre
     * @param color
     * @return 
     */
    private static boolean estaEnlaLista(ArrayList <Bebida> lista, String nombre,
            String color) {
        boolean resultado = false ;
        
             
        String s;
        Iterator<Bebida> e = lista.iterator();
        
        while (e.hasNext()) {
            
            Bebida beb = e.next() ;
            
            String nom = beb.getNombre() ;
            String col = beb.getColor() ;
            //System.out.println(e.toString());
            // Si coincide el nombre
            if (nombre.equalsIgnoreCase(nom)) {
                if (color.equalsIgnoreCase(col)) {
                    resultado = true ;
                }
            }
            
        } 
        return resultado ;
    }
       
}