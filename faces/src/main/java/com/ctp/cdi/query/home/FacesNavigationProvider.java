package com.ctp.cdi.query.home;

import javax.enterprise.inject.Alternative;
import javax.faces.context.FacesContext;

@Alternative
public class FacesNavigationProvider implements NavigationProvider {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean isPostback() {
        return FacesContext.getCurrentInstance().isPostback();
    }

    @Override
    public Object create() {
        return "create?faces-redirect=true";
    }

    @Override
    public Object view(Object primaryKey) {
        return "view?faces-redirect=true&id=" + primaryKey;
    }

    @Override
    public Object exception() {
        return null;
    }

    @Override
    public Object search() {
        return "search?faces-redirect=true";
    }

}
