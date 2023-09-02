package org.acme.entities;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@NamedQuery(name = "Users.findAll", query = "SELECT f FROM org.acme.entities.User f ORDER BY f.name", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))

public class User
{
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private int age;
    private String email;

    public User(String name, int age, String email)
    {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getAge()
    {
        return age;
    }

    public String getEmail()
    {
        return email;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }



}
