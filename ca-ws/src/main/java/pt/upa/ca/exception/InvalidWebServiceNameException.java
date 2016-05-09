package pt.upa.ca.exception;


import pt.upa.ca.exception.CAServerException;

public class InvalidWebServiceNameException extends CAServerException {
    private static final long serialVersionUID = 1L;

    private String name;

    public InvalidWebServiceNameException(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getMessage(){
        return "This WebService name doesnt exist: " + getName();
    }
}