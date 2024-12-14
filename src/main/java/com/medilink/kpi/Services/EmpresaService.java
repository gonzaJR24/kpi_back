package com.medilink.kpi.Services;

import com.medilink.kpi.entities.Empresa;
import com.medilink.kpi.entities.Presupuesto;
import com.medilink.kpi.repositories.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository repository;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private PresupuestoService presupuestoService;

    public void save(Empresa empresa){
      // Obtener la lista actualizada de empleados y recalcular
      List<Presupuesto> presupuestos = presupuestoService.list();
      Presupuesto ultimo_presupuesto = presupuestos.get(presupuestos.size() - 1);
      empleadoService.actualizarPorcentaje(ultimo_presupuesto);
      repository.save(empresa);
    }

    public List<Empresa> list(){
        return repository.findAll();
    }

    public Empresa findById(int id){
        return repository.findById(id).orElse(null);
    }

    public void delete(){
        repository.deleteAll();
    }

    public void edit(Empresa empresa){
        Empresa empresa1=this.findById(1);
        empresa1.setNombreEmpresa(empresa.getNombreEmpresa());
        empresa1.setProgresoEmpresa(empresa.getProgresoEmpresa());
        empresa1.setValorMeta(empresa.getValorMeta());
        repository.save(empresa1);
    }
}
