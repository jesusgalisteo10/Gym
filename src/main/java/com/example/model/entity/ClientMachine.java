package com.example.model.entity;

public class ClientMachine {
    private String clientCode;
    private String machineCode;

    // Constructor vacío
    public ClientMachine() {
    }

    // Constructor con parámetros
    public ClientMachine(String clientCode, String machineCode) {
        this.clientCode = clientCode;
        this.machineCode = machineCode;
    }

    // Getters y Setters
    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    // Método toString()
    @Override
    public String toString() {
        return "ClientMachine{" +
                "clientCode='" + clientCode + '\'' +
                ", machineCode='" + machineCode + '\'' +
                '}';
    }

    // Métodos equals() y hashCode() si es necesario
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientMachine that = (ClientMachine) o;

        if (clientCode != null ? !clientCode.equals(that.clientCode) : that.clientCode != null) return false;
        return machineCode != null ? machineCode.equals(that.machineCode) : that.machineCode == null;
    }

    @Override
    public int hashCode() {
        int result = clientCode != null ? clientCode.hashCode() : 0;
        result = 31 * result + (machineCode != null ? machineCode.hashCode() : 0);
        return result;
    }
}

