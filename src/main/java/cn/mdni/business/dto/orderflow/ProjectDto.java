package cn.mdni.business.dto.orderflow;

/**
 * Created by Allen on 2017/11/23.
 */
public class ProjectDto {
    private OrderDto order;

    private OrderPlaceOrderDto placeOrder;

    private CustomerDto customer;

    private RemarkDto remark;

    public OrderDto getOrder() {
        return order;
    }

    public void setOrder(OrderDto order) {
        this.order = order;
    }

    public OrderPlaceOrderDto getPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(OrderPlaceOrderDto placeOrder) {
        this.placeOrder = placeOrder;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public RemarkDto getRemark() {
        return remark;
    }

    public void setRemark(RemarkDto remark) {
        this.remark = remark;
    }
}
