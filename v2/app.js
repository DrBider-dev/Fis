let clientes = [];
let pedidos = [];
let articulos = [];

function mostrarVista(vista) {
  document.querySelectorAll('.vista').forEach(section => section.style.display = 'none');
  document.getElementById(`vista-${vista}`).style.display = 'block';
}

mostrarVista('cliente');

// CLIENTES
document.getElementById('form-cliente').addEventListener('submit', function(e) {
  e.preventDefault();
  const cliente = {
    codigo: document.getElementById('codigoCliente').value,
    nombre: document.getElementById('nombreCliente').value,
    direccion: document.getElementById('direccionCliente').value,
    saldo: document.getElementById('saldoCliente').value,
    limite: document.getElementById('limiteCliente').value,
    descuento: document.getElementById('descuentoCliente').value
  };
  clientes.push(cliente);
  this.reset();
  alert("Cliente guardado correctamente.");
});

function consultarClientes() {
  const tabla = document.getElementById('tablaClientes');
  tabla.innerHTML = "<tr><th>Código</th><th>Nombre</th><th>Dirección</th><th>Saldo</th><th>Límite</th><th>% Desc</th></tr>";
  clientes.forEach(c => {
    tabla.innerHTML += `<tr><td>${c.codigo}</td><td>${c.nombre}</td><td>${c.direccion}</td><td>${c.saldo}</td><td>${c.limite}</td><td>${c.descuento}</td></tr>`;
  });
}

// PEDIDOS
document.getElementById('form-pedido').addEventListener('submit', function(e) {
  e.preventDefault();
  const pedido = {
    cliente: document.getElementById('clientePedido').value,
    direccion: document.getElementById('direccionPedido').value,
    fecha: document.getElementById('fechaPedido').value,
    codigoArticulo: document.getElementById('codigoArticuloPedido').value,
    cantidad: document.getElementById('cantidadOrdenada').value,
    pendiente: document.getElementById('cantidadPendiente').value
  };
  pedidos.push(pedido);
  this.reset();
  alert("Pedido registrado.");
});

function consultarPedidos() {
  const tabla = document.getElementById('tablaPedidos');
  tabla.innerHTML = "<tr><th>Cliente</th><th>Dirección</th><th>Fecha</th><th>Artículo</th><th>Cantidad</th><th>Pendiente</th></tr>";
  pedidos.forEach(p => {
    tabla.innerHTML += `<tr><td>${p.cliente}</td><td>${p.direccion}</td><td>${p.fecha}</td><td>${p.codigoArticulo}</td><td>${p.cantidad}</td><td>${p.pendiente}</td></tr>`;
  });
}

// ARTÍCULOS
document.getElementById('form-articulo').addEventListener('submit', function(e) {
  e.preventDefault();
  const articulo = {
    codigo: document.getElementById('codigoArticulo').value,
    nombre: document.getElementById('nombreArticulo').value,
    descripcion: document.getElementById('descripcionArticulo').value,
    plantas: document.getElementById('plantasArticulo').value,
    existencia: document.getElementById('existenciaArticulo').value,
    stock: document.getElementById('stockMinimo').value
  };
  articulos.push(articulo);
  this.reset();
  alert("Artículo registrado.");
});

function consultarArticulos() {
  const tabla = document.getElementById('tablaArticulos');
  tabla.innerHTML = "<tr><th>Código</th><th>Nombre</th><th>Descripción</th><th>Plantas</th><th>Existencia</th><th>Stock Mínimo</th></tr>";
  articulos.forEach(a => {
    tabla.innerHTML += `<tr><td>${a.codigo}</td><td>${a.nombre}</td><td>${a.descripcion}</td><td>${a.plantas}</td><td>${a.existencia}</td><td>${a.stock}</td></tr>`;
  });
}
