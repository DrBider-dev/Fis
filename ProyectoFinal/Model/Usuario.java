package Model;

public abstract class Usuario {
    private int id;
    private String nombre;
    private String tot; // Tipo de usuario (TOT)
    private String credenciales;

    public Usuario(int id, String nombre, String tot, String credenciales) {
        this.id = id;
        this.nombre = nombre;
        this.tot = tot;
        this.credenciales = credenciales;
    }

    public boolean autenticar(String credenciales) {
        return this.credenciales.equals(credenciales);
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTot() { return tot; }
    public String getCredenciales() { return credenciales; }
}