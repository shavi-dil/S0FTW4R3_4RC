import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OrderHistoryPanel extends JPanel {
    private final Customer customer;
    private final DefaultTableModel historyModel;
    private final JTable historyTable;

    public OrderHistoryPanel(Customer customer) {
        this.customer = customer;
        setLayout(new BorderLayout(16, 16));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel title = new JLabel("Order history for your account");
        title.setForeground(Theme.TEXT_DARK);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        historyModel = new DefaultTableModel(new Object[]{"Order #", "Date", "Items", "Total", "Status", "Shipment"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        historyTable = new JTable(historyModel);
        historyTable.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        add(scrollPane, BorderLayout.CENTER);

        refreshOrderHistory();
    }

    public void refreshOrderHistory() {
        historyModel.setRowCount(0);
        for (Order order : customer.getOrders()) {
            historyModel.addRow(new Object[]{
                    order.getOrderNumber(),
                    order.getCreatedAtText(),
                    order.getItems().size(),
                    String.format("$%.2f", order.getGrandTotal()),
                    order.getStatus(),
                    order.getShipment().getStatus()
            });
        }
    }
}
