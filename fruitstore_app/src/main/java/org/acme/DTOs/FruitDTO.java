package org.acme.DTOs;

public class FruitDTO
{
    private String name;

    public FruitDTO(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
