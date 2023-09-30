package javafxbebidas;

import java.io.Serializable;

/**
 * Representación de las bebidas de un comercio. Una bebida tendrá un código,
 * un nombre, un color y el número de unidades que hay en stock.
 * @author Jose Cabello
 */
public class Bebida implements Serializable {
    
    private final String nombre ;
    private final String color ;
    private final int unidades ;
    
    private static final long serialVersionUID = 46L ;

    /**
     * Constructor de la clase Bebida. 
    
     * @param nombre    Nombre de la bebida.
     * @param color     Color de la bebida.
     * @param unidades  Unidades en stock de la bebida.
     */
    public Bebida(String nombre, String color, int unidades) {
    
        this.nombre = nombre;
        this.color = color;
        this.unidades = unidades;
    }

    
    
    @Override
    public String toString() {
        return this.nombre + " " + this.color + " " + this.unidades ;
    }

    public String getNombre() {
        return nombre;
    }

    public String getColor() {
        return color;
    }

    public int getUnidades() {
        return unidades;
    }

    
    
}
