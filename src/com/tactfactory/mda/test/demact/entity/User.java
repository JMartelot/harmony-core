/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.mda.test.demact.entity;

import org.joda.time.DateTime;

import com.tactfactory.mda.orm.annotation.*;
import com.tactfactory.mda.orm.annotation.Column.Type;
import com.tactfactory.mda.rest.annotation.Rest;

@Table
@Entity
@Rest(security=Rest.Security.SESSION, uri="user-uri")
public class User extends Object implements Cloneable {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 7032873279928549706L;

	@Id
    @Column(type=Type.INTEGER, hidden=true)
    @GeneratedValue(strategy="IDENTITY")
    protected int id;

	@Column(unique=true)		// type="string", length=255
    protected String login;
	
	@Column(type=Type.STRING)	// type="string", length=255
    protected String password;
	
	@Column(nullable=true)		// type="string", length=255
    protected String firstname;
	
	@Column()					// type="string", length=255
    protected String lastname;
	
	@Column(name="created_at")	// type="datetime",
    protected DateTime createdAt;


	public User() {
		this.id = -1;
    	this.createdAt = new DateTime();
    }
	
	/**
	 * @return the id
	 */
	public final int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public final void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the login
	 */
	public final String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public final void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public final String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public final void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the firstname
	 */
	public final String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public final void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public final String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public final void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return the createdAt
	 */
	public final DateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public final void setCreatedAt(DateTime createdAt) {
		this.createdAt = createdAt;
	}
}
