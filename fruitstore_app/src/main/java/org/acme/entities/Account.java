package org.acme.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.QueryHint;

@Entity
@NamedQuery(name = "Accounts.findAll", query = "SELECT f FROM org.acme.entities.Account f ORDER BY f.name", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))

public class Account
{
    @Id
    @GeneratedValue
    private Integer id;
    @Column(length = 40, unique = true)
    private String name;
    @Column
    private Integer age;
    @Column(length = 40, unique = true)
    private String email;

    public Account(String name, int age, String email)
    {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public Account()
    {
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
