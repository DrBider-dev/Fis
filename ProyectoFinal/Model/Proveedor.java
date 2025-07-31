package Model;

public class Proveedor {
    private int id;
    private String nombre;
    private String contacto;

    public Proveedor(int id, String nombre, String contacto) {
        this.id = id;
        this.nombre = nombre;
        this.contacto = contacto;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getContacto() { return contacto; }
}