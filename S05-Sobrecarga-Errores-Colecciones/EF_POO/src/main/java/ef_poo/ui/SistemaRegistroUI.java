package ef_poo.ui;

import ef_poo.excepciones.ClienteException;
import ef_poo.excepciones.ProductoException;
import ef_poo.excepciones.VentaException;
import ef_poo.modelo.Cliente;
import ef_poo.modelo.DetalleVenta;
import ef_poo.modelo.Producto;
import ef_poo.modelo.ProductoBazar;
import ef_poo.modelo.ProductoPapeleria;
import ef_poo.modelo.ProductoUtilEscolar;
import ef_poo.modelo.Venta;
import ef_poo.servicio.ClienteServicio;
import ef_poo.servicio.ProductoServicio;
import ef_poo.servicio.VentaServicio;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class SistemaRegistroUI extends javax.swing.JFrame {

    private static final ClienteServicio clienteServicio = new ClienteServicio();
    private static final ProductoServicio productoServicio = new ProductoServicio();
    private static final VentaServicio ventaServicio = new VentaServicio(clienteServicio, productoServicio);

    private Venta ventaActual;
    private Producto productoSeleccionado;

    public SistemaRegistroUI() {
        initComponents();
        configurarVentana();
        actualizarCamposProducto();
        refrescarClientes();
        refrescarProductos();
        refrescarVentas();
        reiniciarVenta();
    }

    private void configurarVentana() {
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaClientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                tablaClientesValueChanged(evt);
            }
        });
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                tablaProductosValueChanged(evt);
            }
        });
        tablaVentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaVentas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                tablaVentasValueChanged(evt);
            }
        });
        setLocationRelativeTo(null);
    }

    private void refrescarClientes() {
        DefaultTableModel modelo = (DefaultTableModel) tablaClientes.getModel();
        modelo.setRowCount(0);
        for (Cliente cliente : clienteServicio.getClientes()) {
            modelo.addRow(new Object[]{
                cliente.getCodigo(),
                cliente.getNombre(),
                cliente.getEmail() == null ? "" : cliente.getEmail()
            });
        }
    }

    private void refrescarProductos() {
        DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
        modelo.setRowCount(0);
        for (Producto producto : productoServicio.getProductos()) {
            modelo.addRow(new Object[]{
                producto.getCodigo(),
                producto.getNombre(),
                producto.getTipo(),
                moneda(producto.calcularPrecioFinal()),
                producto.getCaracteristicaEspecial()
            });
        }
    }

    private void refrescarVentas() {
        List<Venta> ventas = ventaServicio.getVentas();
        pintarVentas(ventas);
        actualizarResumenVentas(ventas);
        tablaVentas.clearSelection();
        txtDetalleVentaSeleccionada.setText("Seleccione una venta para ver el detalle.");
    }

    private void pintarVentas(List<Venta> lista) {
        DefaultTableModel modelo = (DefaultTableModel) tablaVentas.getModel();
        modelo.setRowCount(0);
        for (Venta venta : lista) {
            modelo.addRow(new Object[]{
                venta.getCodigo(),
                venta.getCliente().getNombre(),
                venta.getFecha(),
                venta.getDetalles().size(),
                moneda(venta.getTotal())
            });
        }
    }

    private void actualizarResumenVentas(List<Venta> ventasMostradas) {
        lblTotalVentas.setText("Total: "
                + moneda(ventaServicio.calcularTotal(ventasMostradas)));
    }

    private void refrescarDetalleVenta() {
        DefaultTableModel modelo = (DefaultTableModel) tablaDetalleVenta.getModel();
        modelo.setRowCount(0);
        if (ventaActual == null) {
            lblVentaTotal.setText("Total: S/ 0.00");
            return;
        }
        for (DetalleVenta detalle : ventaActual.getDetalles()) {
            Producto producto = detalle.getProducto();
            modelo.addRow(new Object[]{
                producto.getNombre(),
                producto.getTipo(),
                moneda(producto.calcularPrecioFinal()),
                detalle.getCantidad(),
                moneda(detalle.getSubtotal())
            });
        }
        lblVentaTotal.setText("Total: " + moneda(ventaActual.getTotal()));
    }

    private void reiniciarVenta() {
        ventaActual = null;
        productoSeleccionado = null;
        txtVentaCliente.setText("");
        txtVentaFecha.setText(LocalDate.now().toString());
        txtVentaProducto.setText("");
        txtVentaCantidad.setText("");
        lblVentaCliente.setText("-");
        lblVentaProducto.setText("-");
        refrescarDetalleVenta();
    }

    private void actualizarCamposProducto() {
        String tipo = (String) cbProductoTipo.getSelectedItem();
        if ("UtilEscolar".equals(tipo)) {
            lblProductoExtra.setText("En campania escolar");
            txtProductoTalla.setEnabled(false);
        } else if ("Papeleria".equals(tipo)) {
            lblProductoExtra.setText("Venta por mayor");
            txtProductoTalla.setEnabled(false);
        } else {
            lblProductoExtra.setText("Temporada alta");
            txtProductoTalla.setEnabled(true);
        }
    }

    private void buscarClienteVenta() {
        Cliente cliente = buscarClienteDesdeTexto(txtVentaCliente.getText());
        lblVentaCliente.setText(cliente == null ? "No encontrado" : cliente.getNombre());
    }

    private void buscarProductoVenta() {
        try {
            int codigo = Integer.parseInt(txtVentaProducto.getText().trim());
            productoSeleccionado = productoServicio.buscarPorCodigo(codigo);
            if (productoSeleccionado == null) {
                lblVentaProducto.setText("No encontrado");
            } else {
                lblVentaProducto.setText(productoSeleccionado.getNombre() + " - "
                        + moneda(productoSeleccionado.calcularPrecioFinal()));
            }
        } catch (NumberFormatException ex) {
            productoSeleccionado = null;
            lblVentaProducto.setText("Codigo invalido");
        }
    }

    private Cliente buscarClienteDesdeTexto(String texto) {
        try {
            return clienteServicio.buscarPorCodigo(Integer.parseInt(texto.trim()));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String moneda(double valor) {
        return String.format("S/ %.2f", valor);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Validacion", JOptionPane.WARNING_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabsPrincipal = new javax.swing.JTabbedPane();
        panelClientes = new javax.swing.JPanel();
        panelClienteFormulario = new javax.swing.JPanel();
        lblClienteCodigo = new javax.swing.JLabel();
        txtClienteCodigo = new javax.swing.JTextField();
        lblClienteNombre = new javax.swing.JLabel();
        txtClienteNombre = new javax.swing.JTextField();
        lblClienteEmail = new javax.swing.JLabel();
        txtClienteEmail = new javax.swing.JTextField();
        btnClienteRegistrar = new javax.swing.JButton();
        btnClienteBuscar = new javax.swing.JButton();
        scrollClientes = new javax.swing.JScrollPane();
        tablaClientes = new javax.swing.JTable();
        panelClienteAcciones = new javax.swing.JPanel();
        btnClienteLimpiar = new javax.swing.JButton();
        btnClienteGuardarCambios = new javax.swing.JButton();
        btnClienteEliminar = new javax.swing.JButton();
        btnClienteActualizar = new javax.swing.JButton();
        panelProductos = new javax.swing.JPanel();
        panelProductoFormulario = new javax.swing.JPanel();
        lblProductoCodigo = new javax.swing.JLabel();
        txtProductoCodigo = new javax.swing.JTextField();
        lblProductoNombre = new javax.swing.JLabel();
        txtProductoNombre = new javax.swing.JTextField();
        lblProductoPrecio = new javax.swing.JLabel();
        txtProductoPrecio = new javax.swing.JTextField();
        lblProductoTipo = new javax.swing.JLabel();
        cbProductoTipo = new javax.swing.JComboBox();
        lblProductoTalla = new javax.swing.JLabel();
        txtProductoTalla = new javax.swing.JTextField();
        lblProductoExtra = new javax.swing.JLabel();
        chkProductoExtra = new javax.swing.JCheckBox();
        scrollProductos = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        panelProductoAcciones = new javax.swing.JPanel();
        btnProductoLimpiar = new javax.swing.JButton();
        btnProductoBuscar = new javax.swing.JButton();
        btnProductoGuardarCambios = new javax.swing.JButton();
        btnProductoEliminar = new javax.swing.JButton();
        btnProductoActualizar = new javax.swing.JButton();
        btnProductoRegistrar = new javax.swing.JButton();
        panelVentas = new javax.swing.JPanel();
        panelVentaSuperior = new javax.swing.JPanel();
        panelVentaCliente = new javax.swing.JPanel();
        panelVentaClienteLinea1 = new javax.swing.JPanel();
        lblVentaClienteCodigo = new javax.swing.JLabel();
        txtVentaCliente = new javax.swing.JTextField();
        btnVentaBuscarCliente = new javax.swing.JButton();
        lblVentaCliente = new javax.swing.JLabel();
        panelVentaClienteLinea2 = new javax.swing.JPanel();
        lblVentaFecha = new javax.swing.JLabel();
        txtVentaFecha = new javax.swing.JTextField();
        panelVentaProducto = new javax.swing.JPanel();
        panelVentaProductoLinea1 = new javax.swing.JPanel();
        lblVentaProductoCodigo = new javax.swing.JLabel();
        txtVentaProducto = new javax.swing.JTextField();
        btnVentaBuscarProducto = new javax.swing.JButton();
        lblVentaProducto = new javax.swing.JLabel();
        panelVentaProductoLinea2 = new javax.swing.JPanel();
        lblVentaCantidad = new javax.swing.JLabel();
        txtVentaCantidad = new javax.swing.JTextField();
        btnVentaAgregar = new javax.swing.JButton();
        scrollDetalleVenta = new javax.swing.JScrollPane();
        tablaDetalleVenta = new javax.swing.JTable();
        panelVentaAcciones = new javax.swing.JPanel();
        lblVentaTotal = new javax.swing.JLabel();
        btnVentaCancelar = new javax.swing.JButton();
        btnVentaConfirmar = new javax.swing.JButton();
        panelConsultaVentas = new javax.swing.JPanel();
        panelConsultaAcciones = new javax.swing.JPanel();
        lblFiltroCliente = new javax.swing.JLabel();
        txtFiltroCliente = new javax.swing.JTextField();
        btnFiltrarCliente = new javax.swing.JButton();
        lblFiltroFecha = new javax.swing.JLabel();
        txtFiltroFecha = new javax.swing.JTextField();
        btnFiltrarFecha = new javax.swing.JButton();
        btnVerTodasVentas = new javax.swing.JButton();
        btnVentasActualizar = new javax.swing.JButton();
        scrollVentas = new javax.swing.JScrollPane();
        tablaVentas = new javax.swing.JTable();
        panelConsultaInferior = new javax.swing.JPanel();
        panelConsultaResumen = new javax.swing.JPanel();
        lblTotalVentas = new javax.swing.JLabel();
        scrollDetalleVentaSeleccionada = new javax.swing.JScrollPane();
        txtDetalleVentaSeleccionada = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Gestion de Clientes y Ventas - Libreria y Bazar HAS");
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        panelClientes.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panelClientes.setMaximumSize(new java.awt.Dimension(800, 600));
        panelClientes.setName(""); // NOI18N
        panelClientes.setLayout(new java.awt.BorderLayout(12, 12));

        panelClienteFormulario.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del cliente"));
        panelClienteFormulario.setLayout(new java.awt.GridLayout(4, 2));

        lblClienteCodigo.setText("Codigo:");
        panelClienteFormulario.add(lblClienteCodigo);
        panelClienteFormulario.add(txtClienteCodigo);

        lblClienteNombre.setText("Nombre:");
        panelClienteFormulario.add(lblClienteNombre);
        panelClienteFormulario.add(txtClienteNombre);

        lblClienteEmail.setText("Email:");
        panelClienteFormulario.add(lblClienteEmail);
        panelClienteFormulario.add(txtClienteEmail);

        btnClienteRegistrar.setText("Registrar");
        btnClienteRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteRegistrarActionPerformed(evt);
            }
        });
        panelClienteFormulario.add(btnClienteRegistrar);

        btnClienteBuscar.setText("Buscar por codigo");
        btnClienteBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteBuscarActionPerformed(evt);
            }
        });
        panelClienteFormulario.add(btnClienteBuscar);

        panelClientes.add(panelClienteFormulario, java.awt.BorderLayout.NORTH);

        scrollClientes.setMaximumSize(new java.awt.Dimension(720, 150));
        scrollClientes.setPreferredSize(new java.awt.Dimension(720, 150));

        tablaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre", "Email"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaClientes.setRowHeight(24);
        tablaClientes.setShowGrid(false);
        scrollClientes.setViewportView(tablaClientes);

        panelClientes.add(scrollClientes, java.awt.BorderLayout.CENTER);

        panelClienteAcciones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnClienteLimpiar.setText("Limpiar");
        btnClienteLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteLimpiarActionPerformed(evt);
            }
        });
        panelClienteAcciones.add(btnClienteLimpiar);

        btnClienteGuardarCambios.setText("Guardar cambios");
        btnClienteGuardarCambios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteGuardarCambiosActionPerformed(evt);
            }
        });
        panelClienteAcciones.add(btnClienteGuardarCambios);

        btnClienteEliminar.setText("Eliminar");
        btnClienteEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteEliminarActionPerformed(evt);
            }
        });
        panelClienteAcciones.add(btnClienteEliminar);

        btnClienteActualizar.setText("Refrescar lista");
        btnClienteActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteActualizarActionPerformed(evt);
            }
        });
        panelClienteAcciones.add(btnClienteActualizar);

        panelClientes.add(panelClienteAcciones, java.awt.BorderLayout.SOUTH);

        tabsPrincipal.addTab("Clientes", panelClientes);

        panelProductos.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panelProductos.setLayout(new java.awt.BorderLayout(12, 12));

        panelProductoFormulario.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del producto"));
        panelProductoFormulario.setLayout(new java.awt.GridLayout(6, 2));

        lblProductoCodigo.setText("Codigo:");
        panelProductoFormulario.add(lblProductoCodigo);
        panelProductoFormulario.add(txtProductoCodigo);

        lblProductoNombre.setText("Nombre:");
        panelProductoFormulario.add(lblProductoNombre);
        panelProductoFormulario.add(txtProductoNombre);

        lblProductoPrecio.setText("Precio base:");
        panelProductoFormulario.add(lblProductoPrecio);
        panelProductoFormulario.add(txtProductoPrecio);

        lblProductoTipo.setText("Tipo:");
        panelProductoFormulario.add(lblProductoTipo);

        cbProductoTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "UtilEscolar", "Papeleria", "Bazar" }));
        cbProductoTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbProductoTipoActionPerformed(evt);
            }
        });
        panelProductoFormulario.add(cbProductoTipo);

        lblProductoTalla.setText("Categoria (solo Bazar):");
        panelProductoFormulario.add(lblProductoTalla);
        panelProductoFormulario.add(txtProductoTalla);

        lblProductoExtra.setText("En campania escolar");
        panelProductoFormulario.add(lblProductoExtra);
        panelProductoFormulario.add(chkProductoExtra);

        panelProductos.add(panelProductoFormulario, java.awt.BorderLayout.NORTH);

        scrollProductos.setMaximumSize(new java.awt.Dimension(720, 150));
        scrollProductos.setPreferredSize(new java.awt.Dimension(720, 150));

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre", "Tipo", "Precio Final", "Caracteristica"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaProductos.setFillsViewportHeight(true);
        tablaProductos.setRowHeight(26);
        scrollProductos.setViewportView(tablaProductos);

        panelProductos.add(scrollProductos, java.awt.BorderLayout.CENTER);

        panelProductoAcciones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnProductoLimpiar.setText("Limpiar");
        btnProductoLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductoLimpiarActionPerformed(evt);
            }
        });
        panelProductoAcciones.add(btnProductoLimpiar);

        btnProductoBuscar.setText("Buscar por codigo");
        btnProductoBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductoBuscarActionPerformed(evt);
            }
        });
        panelProductoAcciones.add(btnProductoBuscar);

        btnProductoGuardarCambios.setText("Guardar cambios");
        btnProductoGuardarCambios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductoGuardarCambiosActionPerformed(evt);
            }
        });
        panelProductoAcciones.add(btnProductoGuardarCambios);

        btnProductoEliminar.setText("Eliminar");
        btnProductoEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductoEliminarActionPerformed(evt);
            }
        });
        panelProductoAcciones.add(btnProductoEliminar);

        btnProductoActualizar.setText("Refrescar lista");
        btnProductoActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductoActualizarActionPerformed(evt);
            }
        });
        panelProductoAcciones.add(btnProductoActualizar);

        btnProductoRegistrar.setText("Registrar Producto");
        btnProductoRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductoRegistrarActionPerformed(evt);
            }
        });
        panelProductoAcciones.add(btnProductoRegistrar);

        panelProductos.add(panelProductoAcciones, java.awt.BorderLayout.SOUTH);

        tabsPrincipal.addTab("Productos", panelProductos);

        panelVentas.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panelVentas.setLayout(new java.awt.BorderLayout(12, 12));

        panelVentaSuperior.setLayout(new java.awt.GridLayout(2, 1));

        panelVentaCliente.setBorder(javax.swing.BorderFactory.createTitledBorder("Cabecera de Venta"));
        panelVentaCliente.setLayout(new java.awt.GridLayout(2, 1));

        panelVentaClienteLinea1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblVentaClienteCodigo.setText("Cod. cliente:");
        panelVentaClienteLinea1.add(lblVentaClienteCodigo);

        txtVentaCliente.setColumns(8);
        panelVentaClienteLinea1.add(txtVentaCliente);

        btnVentaBuscarCliente.setText("Buscar cliente");
        btnVentaBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentaBuscarClienteActionPerformed(evt);
            }
        });
        panelVentaClienteLinea1.add(btnVentaBuscarCliente);

        lblVentaCliente.setText("-");
        panelVentaClienteLinea1.add(lblVentaCliente);

        panelVentaCliente.add(panelVentaClienteLinea1);

        panelVentaClienteLinea2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblVentaFecha.setText("Fecha:");
        panelVentaClienteLinea2.add(lblVentaFecha);

        txtVentaFecha.setColumns(10);
        panelVentaClienteLinea2.add(txtVentaFecha);

        panelVentaCliente.add(panelVentaClienteLinea2);

        panelVentaSuperior.add(panelVentaCliente);

        panelVentaProducto.setBorder(javax.swing.BorderFactory.createTitledBorder("Agregar Detalle"));
        panelVentaProducto.setLayout(new java.awt.GridLayout(2, 1));

        panelVentaProductoLinea1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblVentaProductoCodigo.setText("Cod. producto:");
        panelVentaProductoLinea1.add(lblVentaProductoCodigo);

        txtVentaProducto.setColumns(8);
        panelVentaProductoLinea1.add(txtVentaProducto);

        btnVentaBuscarProducto.setText("Buscar producto");
        btnVentaBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentaBuscarProductoActionPerformed(evt);
            }
        });
        panelVentaProductoLinea1.add(btnVentaBuscarProducto);

        lblVentaProducto.setText("-");
        panelVentaProductoLinea1.add(lblVentaProducto);

        panelVentaProducto.add(panelVentaProductoLinea1);

        panelVentaProductoLinea2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblVentaCantidad.setText("Cantidad:");
        panelVentaProductoLinea2.add(lblVentaCantidad);

        txtVentaCantidad.setColumns(5);
        panelVentaProductoLinea2.add(txtVentaCantidad);

        btnVentaAgregar.setText("Agregar");
        btnVentaAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentaAgregarActionPerformed(evt);
            }
        });
        panelVentaProductoLinea2.add(btnVentaAgregar);

        panelVentaProducto.add(panelVentaProductoLinea2);

        panelVentaSuperior.add(panelVentaProducto);

        panelVentas.add(panelVentaSuperior, java.awt.BorderLayout.NORTH);

        scrollDetalleVenta.setMaximumSize(new java.awt.Dimension(720, 150));
        scrollDetalleVenta.setPreferredSize(new java.awt.Dimension(720, 150));

        tablaDetalleVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Tipo", "P. Unit.", "Cant.", "Subtotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaDetalleVenta.setFillsViewportHeight(true);
        tablaDetalleVenta.setRowHeight(26);
        scrollDetalleVenta.setViewportView(tablaDetalleVenta);

        panelVentas.add(scrollDetalleVenta, java.awt.BorderLayout.CENTER);

        panelVentaAcciones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        lblVentaTotal.setText("Total: S/ 0.00");
        panelVentaAcciones.add(lblVentaTotal);

        btnVentaCancelar.setText("Cancelar");
        btnVentaCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentaCancelarActionPerformed(evt);
            }
        });
        panelVentaAcciones.add(btnVentaCancelar);

        btnVentaConfirmar.setText("Confirmar Venta");
        btnVentaConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentaConfirmarActionPerformed(evt);
            }
        });
        panelVentaAcciones.add(btnVentaConfirmar);

        panelVentas.add(panelVentaAcciones, java.awt.BorderLayout.SOUTH);

        tabsPrincipal.addTab("Registrar Venta", panelVentas);

        panelConsultaVentas.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panelConsultaVentas.setLayout(new java.awt.BorderLayout(12, 12));

        panelConsultaAcciones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblFiltroCliente.setText("Cliente (codigo):");
        panelConsultaAcciones.add(lblFiltroCliente);

        txtFiltroCliente.setColumns(6);
        panelConsultaAcciones.add(txtFiltroCliente);

        btnFiltrarCliente.setText("Filtrar por cliente");
        btnFiltrarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarClienteActionPerformed(evt);
            }
        });
        panelConsultaAcciones.add(btnFiltrarCliente);

        lblFiltroFecha.setText("Fecha:");
        panelConsultaAcciones.add(lblFiltroFecha);

        txtFiltroFecha.setColumns(10);
        panelConsultaAcciones.add(txtFiltroFecha);

        btnFiltrarFecha.setText("Filtrar por fecha");
        btnFiltrarFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarFechaActionPerformed(evt);
            }
        });
        panelConsultaAcciones.add(btnFiltrarFecha);

        btnVerTodasVentas.setText("Ver todas");
        btnVerTodasVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerTodasVentasActionPerformed(evt);
            }
        });
        panelConsultaAcciones.add(btnVerTodasVentas);

        btnVentasActualizar.setText("Actualizar lista");
        btnVentasActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasActualizarActionPerformed(evt);
            }
        });
        panelConsultaAcciones.add(btnVentasActualizar);

        panelConsultaVentas.add(panelConsultaAcciones, java.awt.BorderLayout.NORTH);

        scrollVentas.setMaximumSize(new java.awt.Dimension(720, 150));
        scrollVentas.setPreferredSize(new java.awt.Dimension(720, 150));

        tablaVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod.", "Cliente", "Fecha", "Items", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaVentas.setFillsViewportHeight(true);
        tablaVentas.setRowHeight(26);
        scrollVentas.setViewportView(tablaVentas);

        panelConsultaVentas.add(scrollVentas, java.awt.BorderLayout.CENTER);

        panelConsultaInferior.setLayout(new java.awt.BorderLayout(8, 8));

        panelConsultaResumen.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblTotalVentas.setText("Total: S/ 0.00");
        panelConsultaResumen.add(lblTotalVentas);

        panelConsultaInferior.add(panelConsultaResumen, java.awt.BorderLayout.NORTH);

        scrollDetalleVentaSeleccionada.setMaximumSize(new java.awt.Dimension(720, 100));
        scrollDetalleVentaSeleccionada.setPreferredSize(new java.awt.Dimension(720, 100));

        txtDetalleVentaSeleccionada.setEditable(false);
        txtDetalleVentaSeleccionada.setColumns(20);
        txtDetalleVentaSeleccionada.setRows(6);
        scrollDetalleVentaSeleccionada.setViewportView(txtDetalleVentaSeleccionada);

        panelConsultaInferior.add(scrollDetalleVentaSeleccionada, java.awt.BorderLayout.CENTER);

        panelConsultaVentas.add(panelConsultaInferior, java.awt.BorderLayout.SOUTH);

        tabsPrincipal.addTab("Consultar Ventas", panelConsultaVentas);

        getContentPane().add(tabsPrincipal, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClienteRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteRegistrarActionPerformed
        String codigo = txtClienteCodigo.getText().trim();
        String nombre = txtClienteNombre.getText().trim();
        String email = txtClienteEmail.getText().trim();
        if (codigo.isEmpty() || nombre.isEmpty()) {
            mostrarError("Codigo y nombre son obligatorios.");
            return;
        }
        try {
            int codigoCliente = Integer.parseInt(codigo);
            if (email.isEmpty()) {
                clienteServicio.registrar(codigoCliente, nombre);
            } else {
                clienteServicio.registrar(codigoCliente, nombre, email);
            }
            txtClienteCodigo.setText("");
            txtClienteNombre.setText("");
            txtClienteEmail.setText("");
            refrescarClientes();
            JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
        } catch (NumberFormatException ex) {
            mostrarError("El codigo debe ser numerico.");
        } catch (ClienteException ex) {
            mostrarError(ex.getMessage());
        }
    }//GEN-LAST:event_btnClienteRegistrarActionPerformed

    private void btnClienteBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteBuscarActionPerformed
        Cliente cliente = buscarClienteDesdeTexto(txtClienteCodigo.getText());
        if (cliente == null) {
            mostrarError("Cliente no encontrado.");
            return;
        }
        txtClienteNombre.setText(cliente.getNombre());
        txtClienteEmail.setText(cliente.getEmail() == null ? "" : cliente.getEmail());
    }//GEN-LAST:event_btnClienteBuscarActionPerformed

    private void btnClienteLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteLimpiarActionPerformed
        limpiarCliente();
    }//GEN-LAST:event_btnClienteLimpiarActionPerformed

    private void btnClienteGuardarCambiosActionPerformed(java.awt.event.ActionEvent evt) {
        String codigo = txtClienteCodigo.getText().trim();
        String nombre = txtClienteNombre.getText().trim();
        String email = txtClienteEmail.getText().trim();
        if (codigo.isEmpty() || nombre.isEmpty()) {
            mostrarError("Codigo y nombre son obligatorios.");
            return;
        }
        try {
            clienteServicio.actualizar(Integer.parseInt(codigo), nombre, email);
            limpiarCliente();
            refrescarClientes();
            JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.");
        } catch (NumberFormatException ex) {
            mostrarError("El codigo debe ser numerico.");
        } catch (ClienteException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void btnClienteEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        String codigo = txtClienteCodigo.getText().trim();
        if (codigo.isEmpty()) {
            mostrarError("Ingrese el codigo del cliente a eliminar.");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "Desea eliminar el cliente " + codigo + "?\n"
                + "Esta accion tambien eliminara todas las ventas registradas de este cliente.",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            clienteServicio.eliminar(Integer.parseInt(codigo));
            limpiarCliente();
            refrescarClientes();
            JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.");
        } catch (NumberFormatException ex) {
            mostrarError("El codigo debe ser numerico.");
        } catch (ClienteException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void btnClienteActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteActualizarActionPerformed
        refrescarClientes();
    }//GEN-LAST:event_btnClienteActualizarActionPerformed

    private void cbProductoTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbProductoTipoActionPerformed
        actualizarCamposProducto();
    }//GEN-LAST:event_cbProductoTipoActionPerformed

    private void btnProductoRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductoRegistrarActionPerformed
        String codigo = txtProductoCodigo.getText().trim();
        String nombre = txtProductoNombre.getText().trim();
        String precio = txtProductoPrecio.getText().trim();
        String tipo = (String) cbProductoTipo.getSelectedItem();
        if (codigo.isEmpty() || nombre.isEmpty() || precio.isEmpty()) {
            mostrarError("Codigo, nombre y precio son obligatorios.");
            return;
        }
        try {
            int codigoProducto = Integer.parseInt(codigo);
            double precioBase = Double.parseDouble(precio);
            boolean extra = chkProductoExtra.isSelected();
            Producto producto;
            if ("UtilEscolar".equals(tipo)) {
                producto = new ProductoUtilEscolar(codigoProducto, nombre, precioBase, extra);
            } else if ("Papeleria".equals(tipo)) {
                producto = new ProductoPapeleria(codigoProducto, nombre, precioBase, extra);
            } else {
                String categoria = txtProductoTalla.getText().trim();
                if (categoria.isEmpty()) {
                    mostrarError("La categoria es obligatoria para productos de bazar.");
                    return;
                }
                producto = new ProductoBazar(codigoProducto, nombre, precioBase, categoria, extra);
            }
            productoServicio.registrar(producto);
            limpiarProducto();
            refrescarProductos();
            JOptionPane.showMessageDialog(this, "Producto registrado correctamente.");
        } catch (NumberFormatException ex) {
            mostrarError("Codigo y precio deben ser numericos.");
        } catch (ProductoException | IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }//GEN-LAST:event_btnProductoRegistrarActionPerformed

    private void btnProductoLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductoLimpiarActionPerformed
        limpiarProducto();
    }//GEN-LAST:event_btnProductoLimpiarActionPerformed

    private void btnProductoBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        Producto producto = buscarProductoFormulario();
        if (producto == null) {
            mostrarError("Producto no encontrado.");
            return;
        }
        cargarProductoEnFormulario(producto);
    }

    private void btnProductoGuardarCambiosActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Producto producto = construirProductoDesdeFormulario();
            productoServicio.actualizar(producto);
            limpiarProducto();
            refrescarProductos();
            JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
        } catch (NumberFormatException ex) {
            mostrarError("Codigo y precio deben ser numericos.");
        } catch (ProductoException | IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void btnProductoEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        String codigo = txtProductoCodigo.getText().trim();
        if (codigo.isEmpty()) {
            mostrarError("Ingrese el codigo del producto a eliminar.");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "Desea eliminar el producto " + codigo + "?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            productoServicio.eliminar(Integer.parseInt(codigo));
            limpiarProducto();
            refrescarProductos();
            JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
        } catch (NumberFormatException ex) {
            mostrarError("El codigo debe ser numerico.");
        } catch (ProductoException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void btnProductoActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductoActualizarActionPerformed
        refrescarProductos();
    }//GEN-LAST:event_btnProductoActualizarActionPerformed

    private void btnVentaBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentaBuscarClienteActionPerformed
        buscarClienteVenta();
    }//GEN-LAST:event_btnVentaBuscarClienteActionPerformed

    private void btnVentaBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentaBuscarProductoActionPerformed
        buscarProductoVenta();
    }//GEN-LAST:event_btnVentaBuscarProductoActionPerformed

    private void btnVentaAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentaAgregarActionPerformed
        Cliente cliente = buscarClienteDesdeTexto(txtVentaCliente.getText());
        if (cliente == null) {
            mostrarError("Busque primero un cliente valido.");
            return;
        }
        if (productoSeleccionado == null) {
            buscarProductoVenta();
            if (productoSeleccionado == null) {
                mostrarError("Busque primero un producto valido.");
                return;
            }
        }
        try {
            int cantidad = Integer.parseInt(txtVentaCantidad.getText().trim());
            if (ventaActual == null) {
                ventaActual = new Venta(ventaServicio.getSiguienteCodigo(), cliente, txtVentaFecha.getText().trim());
            }
            ventaServicio.agregarDetalle(ventaActual, productoSeleccionado, cantidad);
            productoSeleccionado = null;
            txtVentaProducto.setText("");
            txtVentaCantidad.setText("");
            lblVentaProducto.setText("-");
            refrescarDetalleVenta();
        } catch (NumberFormatException ex) {
            mostrarError("La cantidad debe ser un numero entero.");
        } catch (VentaException ex) {
            mostrarError(ex.getMessage());
        }
    }//GEN-LAST:event_btnVentaAgregarActionPerformed

    private void btnVentaCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentaCancelarActionPerformed
        reiniciarVenta();
    }//GEN-LAST:event_btnVentaCancelarActionPerformed

    private void btnVentaConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentaConfirmarActionPerformed
        if (ventaActual == null || ventaActual.getDetalles().isEmpty()) {
            mostrarError("Agregue al menos un producto a la venta.");
            return;
        }
        try {
            ventaServicio.registrarVenta(ventaActual);
            JOptionPane.showMessageDialog(this, "Venta registrada. Total: " + moneda(ventaActual.getTotal()));
            reiniciarVenta();
            refrescarVentas();
            tabsPrincipal.setSelectedComponent(panelConsultaVentas);
        } catch (VentaException ex) {
            mostrarError(ex.getMessage());
        }
    }//GEN-LAST:event_btnVentaConfirmarActionPerformed

    private void btnVentasActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasActualizarActionPerformed
        refrescarVentas();
    }//GEN-LAST:event_btnVentasActualizarActionPerformed

    private void btnFiltrarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarClienteActionPerformed
        String texto = txtFiltroCliente.getText().trim();
        if (texto.isEmpty()) {
            mostrarError("Ingrese el codigo del cliente a filtrar.");
            return;
        }
        try {
            int codigo = Integer.parseInt(texto);
            List<Venta> ventasCliente = ventaServicio.listarPorCliente(codigo);
            pintarVentas(ventasCliente);
            actualizarResumenVentas(ventasCliente);
            tablaVentas.clearSelection();
            txtDetalleVentaSeleccionada.setText(ventasCliente.isEmpty()
                    ? "El cliente " + codigo + " no tiene ventas registradas."
                    : "Mostrando " + ventasCliente.size() + " venta(s) del cliente " + codigo
                    + ". Total: " + moneda(ventaServicio.calcularTotal(ventasCliente)) + ".");
        } catch (NumberFormatException ex) {
            mostrarError("El codigo del cliente debe ser numerico.");
        }
    }//GEN-LAST:event_btnFiltrarClienteActionPerformed

    private void btnFiltrarFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarFechaActionPerformed
        String texto = txtFiltroFecha.getText().trim();
        if (texto.isEmpty()) {
            mostrarError("Ingrese la fecha a filtrar con formato yyyy-MM-dd.");
            return;
        }
        try {
            String fecha = LocalDate.parse(texto).toString();
            List<Venta> ventasFecha = ventaServicio.listarPorFecha(fecha);
            pintarVentas(ventasFecha);
            actualizarResumenVentas(ventasFecha);
            tablaVentas.clearSelection();
            txtDetalleVentaSeleccionada.setText(ventasFecha.isEmpty()
                    ? "La fecha " + fecha + " no tiene ventas registradas."
                    : "Mostrando " + ventasFecha.size() + " venta(s) de la fecha " + fecha
                    + ". Total: " + moneda(ventaServicio.calcularTotal(ventasFecha)) + ".");
        } catch (DateTimeParseException ex) {
            mostrarError("La fecha debe tener formato yyyy-MM-dd. Ejemplo: 2026-06-01.");
        }
    }//GEN-LAST:event_btnFiltrarFechaActionPerformed

    private void btnVerTodasVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerTodasVentasActionPerformed
        txtFiltroCliente.setText("");
        txtFiltroFecha.setText("");
        refrescarVentas();
    }//GEN-LAST:event_btnVerTodasVentasActionPerformed

    private void tablaVentasValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting()) {
            return;
        }
        int fila = tablaVentas.getSelectedRow();
        if (fila < 0) {
            return;
        }
        Object valorCodigo = tablaVentas.getValueAt(fila, 0);
        int codigo = (Integer) valorCodigo;
        Venta venta = ventaServicio.buscarPorCodigo(codigo);
        if (venta == null) {
            return;
        }
        StringBuilder detalle = new StringBuilder();
        detalle.append("Venta #").append(venta.getCodigo())
                .append(" | Cliente: ").append(venta.getCliente().getNombre())
                .append(" | Fecha: ").append(venta.getFecha()).append(System.lineSeparator());
        detalle.append("--------------------------------------------------").append(System.lineSeparator());
        for (DetalleVenta item : venta.getDetalles()) {
            detalle.append(String.format("%-20s x%d = %s%n",
                    item.getProducto().getNombre(),
                    item.getCantidad(),
                    moneda(item.getSubtotal())));
        }
        detalle.append("--------------------------------------------------").append(System.lineSeparator());
        detalle.append("TOTAL: ").append(moneda(venta.getTotal()));
        txtDetalleVentaSeleccionada.setText(detalle.toString());
    }

    private void tablaClientesValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting()) {
            return;
        }
        int fila = tablaClientes.getSelectedRow();
        if (fila < 0) {
            return;
        }
        Object valorCodigo = tablaClientes.getValueAt(fila, 0);
        int codigo = (Integer) valorCodigo;
        Cliente cliente = clienteServicio.buscarPorCodigo(codigo);
        if (cliente != null) {
            txtClienteCodigo.setText(String.valueOf(cliente.getCodigo()));
            txtClienteNombre.setText(cliente.getNombre());
            txtClienteEmail.setText(cliente.getEmail() == null ? "" : cliente.getEmail());
        }
    }

    private void tablaProductosValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting()) {
            return;
        }
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) {
            return;
        }
        Object valorCodigo = tablaProductos.getValueAt(fila, 0);
        int codigo = (Integer) valorCodigo;
        Producto producto = productoServicio.buscarPorCodigo(codigo);
        if (producto != null) {
            cargarProductoEnFormulario(producto);
        }
    }

    private void limpiarCliente() {
        txtClienteCodigo.setText("");
        txtClienteNombre.setText("");
        txtClienteEmail.setText("");
        tablaClientes.clearSelection();
    }

    private void limpiarProducto() {
        txtProductoCodigo.setText("");
        txtProductoNombre.setText("");
        txtProductoPrecio.setText("");
        txtProductoTalla.setText("");
        chkProductoExtra.setSelected(false);
        cbProductoTipo.setSelectedIndex(0);
        actualizarCamposProducto();
        tablaProductos.clearSelection();
    }

    private Producto buscarProductoFormulario() {
        try {
            return productoServicio.buscarPorCodigo(Integer.parseInt(txtProductoCodigo.getText().trim()));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Producto construirProductoDesdeFormulario() {
        String codigo = txtProductoCodigo.getText().trim();
        String nombre = txtProductoNombre.getText().trim();
        String precio = txtProductoPrecio.getText().trim();
        String tipo = (String) cbProductoTipo.getSelectedItem();
        if (codigo.isEmpty() || nombre.isEmpty() || precio.isEmpty()) {
            throw new IllegalArgumentException("Codigo, nombre y precio son obligatorios.");
        }
        int codigoProducto = Integer.parseInt(codigo);
        double precioBase = Double.parseDouble(precio);
        boolean extra = chkProductoExtra.isSelected();
        if ("UtilEscolar".equals(tipo)) {
            return new ProductoUtilEscolar(codigoProducto, nombre, precioBase, extra);
        }
        if ("Papeleria".equals(tipo)) {
            return new ProductoPapeleria(codigoProducto, nombre, precioBase, extra);
        }
        String categoria = txtProductoTalla.getText().trim();
        if (categoria.isEmpty()) {
            throw new IllegalArgumentException("La categoria es obligatoria para productos de bazar.");
        }
        return new ProductoBazar(codigoProducto, nombre, precioBase, categoria, extra);
    }

    private void cargarProductoEnFormulario(Producto producto) {
        txtProductoCodigo.setText(String.valueOf(producto.getCodigo()));
        txtProductoNombre.setText(producto.getNombre());
        txtProductoPrecio.setText(String.valueOf(producto.getPrecioBase()));
        txtProductoTalla.setText("");
        chkProductoExtra.setSelected(false);
        // Producto no expone atributos propios de un solo subtipo; el discriminador
        // getTipo() indica cual es el subtipo real antes de convertir (cast) a la
        // clase concreta correspondiente para leer su atributo propio.
        String tipo = producto.getTipo();
        cbProductoTipo.setSelectedItem(tipo);
        if ("UtilEscolar".equals(tipo)) {
            chkProductoExtra.setSelected(((ProductoUtilEscolar) producto).isEnCampaniaEscolar());
        } else if ("Papeleria".equals(tipo)) {
            chkProductoExtra.setSelected(((ProductoPapeleria) producto).isVentaPorMayor());
        } else if ("Bazar".equals(tipo)) {
            ProductoBazar bazar = (ProductoBazar) producto;
            txtProductoTalla.setText(bazar.getCategoria());
            chkProductoExtra.setSelected(bazar.isTemporadaAlta());
        }
        actualizarCamposProducto();
    }

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
            System.err.println("No se pudo aplicar Nimbus: " + ex.getMessage());
        }

        SwingUtilities.invokeLater(() -> new SistemaRegistroUI().setVisible(true));
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClienteActualizar;
    private javax.swing.JButton btnClienteBuscar;
    private javax.swing.JButton btnClienteEliminar;
    private javax.swing.JButton btnClienteGuardarCambios;
    private javax.swing.JButton btnClienteLimpiar;
    private javax.swing.JButton btnClienteRegistrar;
    private javax.swing.JButton btnFiltrarCliente;
    private javax.swing.JButton btnFiltrarFecha;
    private javax.swing.JButton btnProductoActualizar;
    private javax.swing.JButton btnProductoBuscar;
    private javax.swing.JButton btnProductoEliminar;
    private javax.swing.JButton btnProductoGuardarCambios;
    private javax.swing.JButton btnProductoLimpiar;
    private javax.swing.JButton btnProductoRegistrar;
    private javax.swing.JButton btnVentaAgregar;
    private javax.swing.JButton btnVentaBuscarCliente;
    private javax.swing.JButton btnVentaBuscarProducto;
    private javax.swing.JButton btnVentaCancelar;
    private javax.swing.JButton btnVentaConfirmar;
    private javax.swing.JButton btnVentasActualizar;
    private javax.swing.JButton btnVerTodasVentas;
    private javax.swing.JComboBox cbProductoTipo;
    private javax.swing.JCheckBox chkProductoExtra;
    private javax.swing.JLabel lblClienteCodigo;
    private javax.swing.JLabel lblClienteEmail;
    private javax.swing.JLabel lblClienteNombre;
    private javax.swing.JLabel lblFiltroCliente;
    private javax.swing.JLabel lblFiltroFecha;
    private javax.swing.JLabel lblProductoCodigo;
    private javax.swing.JLabel lblProductoExtra;
    private javax.swing.JLabel lblProductoNombre;
    private javax.swing.JLabel lblProductoPrecio;
    private javax.swing.JLabel lblProductoTalla;
    private javax.swing.JLabel lblProductoTipo;
    private javax.swing.JLabel lblTotalVentas;
    private javax.swing.JLabel lblVentaCantidad;
    private javax.swing.JLabel lblVentaCliente;
    private javax.swing.JLabel lblVentaClienteCodigo;
    private javax.swing.JLabel lblVentaFecha;
    private javax.swing.JLabel lblVentaProducto;
    private javax.swing.JLabel lblVentaProductoCodigo;
    private javax.swing.JLabel lblVentaTotal;
    private javax.swing.JPanel panelClienteAcciones;
    private javax.swing.JPanel panelClienteFormulario;
    private javax.swing.JPanel panelClientes;
    private javax.swing.JPanel panelConsultaAcciones;
    private javax.swing.JPanel panelConsultaInferior;
    private javax.swing.JPanel panelConsultaResumen;
    private javax.swing.JPanel panelConsultaVentas;
    private javax.swing.JPanel panelProductoAcciones;
    private javax.swing.JPanel panelProductoFormulario;
    private javax.swing.JPanel panelProductos;
    private javax.swing.JPanel panelVentaAcciones;
    private javax.swing.JPanel panelVentaCliente;
    private javax.swing.JPanel panelVentaClienteLinea1;
    private javax.swing.JPanel panelVentaClienteLinea2;
    private javax.swing.JPanel panelVentaProducto;
    private javax.swing.JPanel panelVentaProductoLinea1;
    private javax.swing.JPanel panelVentaProductoLinea2;
    private javax.swing.JPanel panelVentaSuperior;
    private javax.swing.JPanel panelVentas;
    private javax.swing.JScrollPane scrollClientes;
    private javax.swing.JScrollPane scrollDetalleVenta;
    private javax.swing.JScrollPane scrollDetalleVentaSeleccionada;
    private javax.swing.JScrollPane scrollProductos;
    private javax.swing.JScrollPane scrollVentas;
    private javax.swing.JTable tablaClientes;
    private javax.swing.JTable tablaDetalleVenta;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTable tablaVentas;
    private javax.swing.JTabbedPane tabsPrincipal;
    private javax.swing.JTextField txtClienteCodigo;
    private javax.swing.JTextField txtClienteEmail;
    private javax.swing.JTextField txtClienteNombre;
    private javax.swing.JTextArea txtDetalleVentaSeleccionada;
    private javax.swing.JTextField txtFiltroCliente;
    private javax.swing.JTextField txtFiltroFecha;
    private javax.swing.JTextField txtProductoCodigo;
    private javax.swing.JTextField txtProductoNombre;
    private javax.swing.JTextField txtProductoPrecio;
    private javax.swing.JTextField txtProductoTalla;
    private javax.swing.JTextField txtVentaCantidad;
    private javax.swing.JTextField txtVentaCliente;
    private javax.swing.JTextField txtVentaFecha;
    private javax.swing.JTextField txtVentaProducto;
    // End of variables declaration//GEN-END:variables

}
