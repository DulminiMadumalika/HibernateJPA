package lk.ijse.pos.dao;

import lk.ijse.pos.entity.SuperEntity;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class CrudDAOImpl<T extends SuperEntity, ID extends Serializable> implements CrudDAO<T,ID>{

    protected EntityManager em;
    private Class<T> entity;


    public CrudDAOImpl(){
        entity = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void setSession(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(T entity) throws Exception {
        em.persist(entity);
    }

    @Override
    public void update(T entity) throws Exception {
        em.merge(entity);
    }

    @Override
    public void delete(ID entityId) throws Exception {
        em.remove(em.find(entity, entityId));
    }

    @Override
    public List<T> findAll() throws Exception {
        return em.createQuery("FROM " + entity.getName()).getResultList();
    }

    @Override
    public T find(ID entityId) throws Exception {
        return em.find(entity,entityId);
    }
}
