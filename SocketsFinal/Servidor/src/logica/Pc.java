package logica;

public class Pc {
    private Integer id;
    private Integer puertoPHP;
    private Integer puertoSQL;

    public Pc(Integer id, Integer puertoPHP, Integer puertoSQL) {
        this.id = id;
        this.puertoPHP = puertoPHP;
        this.puertoSQL = puertoSQL;
    }

    public Pc() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPuertoPHP() {
        return puertoPHP;
    }

    public void setPuertoPHP(Integer puertoPHP) {
        this.puertoPHP = puertoPHP;
    }

    public Integer getPuertoSQL() {
        return puertoSQL;
    }

    public void setPuertoSQL(Integer puertoSQL) {
        this.puertoSQL = puertoSQL;
    }

    @Override
    public String toString() {
        return "Pc{" + "id=" + id + ", puertoPHP=" + puertoPHP + ", puertoSQL=" + puertoSQL + '}';
    }
    
    
}
