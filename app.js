function mostrarVista(vista) {
  document.querySelectorAll('.vista').forEach(section => {
    section.style.display = 'none';
  });

  switch(vista) {
    case 'cliente':
      document.getElementById('vista-cliente').style.display = 'block';
      break;
    case 'ventas':
      document.getElementById('vista-ventas').style.display = 'block';
      break;
    case 'proveedor':
      document.getElementById('vista-proveedor').style.display = 'block';
      break;
  }
}

// Mostrar vista por defecto
mostrarVista('cliente');
