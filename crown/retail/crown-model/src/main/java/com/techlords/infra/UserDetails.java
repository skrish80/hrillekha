package com.techlords.infra;

import java.util.Collection;

public interface UserDetails extends java.io.Serializable {
  
  public abstract Collection<String> getAuthorities();
  
  public abstract String getPassword();
  
  public abstract java.lang.String getUsername();
  
  public abstract boolean isAccountNonExpired();
  
  public abstract boolean isAccountNonLocked();
  
  public abstract boolean isCredentialsNonExpired();
  
  public abstract boolean isEnabled();
}