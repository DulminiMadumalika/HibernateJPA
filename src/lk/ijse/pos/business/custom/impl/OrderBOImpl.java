package lk.ijse.pos.business.custom.impl;

import lk.ijse.pos.business.custom.OrderBO;
import lk.ijse.pos.dao.DAOFactory;
import lk.ijse.pos.dao.DAOTypes;
import lk.ijse.pos.dao.custom.CustomerDAO;
import lk.ijse.pos.dao.custom.ItemDAO;
import lk.ijse.pos.dao.custom.OrderDAO;
import lk.ijse.pos.dao.custom.OrderDetailDAO;
import lk.ijse.pos.db.HibernateUtil;
import lk.ijse.pos.dto.OrderDTO;
import lk.ijse.pos.dto.OrderDetailDTO;
import lk.ijse.pos.entity.Customer;
import lk.ijse.pos.entity.Item;
import lk.ijse.pos.entity.Order;
import lk.ijse.pos.entity.OrderDetail;
import org.hibernate.Session;

import javax.persistence.EntityManager;

public class OrderBOImpl implements OrderBO {

    private OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOTypes.ORDER);
    private OrderDetailDAO orderDetailDAO = DAOFactory.getInstance().getDAO(DAOTypes.ORDER_DETAIL);
    private ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOTypes.ITEM);
    private CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOTypes.CUSTOMER);

    public void placeOrder(OrderDTO order) throws Exception {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        orderDAO.setSession(em);
        customerDAO.setSession(em);
        itemDAO.setSession(em);
        orderDetailDAO.setSession(em);

        // Find the customer
        Customer customer = customerDAO.find(order.getCustomerId());
        // Save the order
        orderDAO.save(new Order(order.getOrderId(), order.getOrderDate(), customer));
        //  Save OrderDetails and Update the Qty.
        for (OrderDetailDTO dto : order.getOrderDetails()) {
            orderDetailDAO.save(new OrderDetail(dto.getOrderId(), dto.getItemCode(), dto.getQty(), dto.getUnitPrice()));
            // Find the item
            Item item = itemDAO.find(dto.getItemCode());
            // Calculate the qty. on hand
            int qty = item.getQtyOnHand() - dto.getQty();
            // Update the new qty.on hand
            item.setQtyOnHand(qty);
        }

        em.getTransaction().commit();
    }


    public int generateOrderId() throws Exception {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            orderDAO.setSession(em);
            return orderDAO.getLastOrderId() + 1;
        } catch (NullPointerException e) {
            return 1;
        }
    }

}
