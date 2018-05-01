package com.peruviansy.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import com.peruviansy.dao.IPersonaDAO;
import com.peruviansy.model.Persona;

//@Named
//@RequestScoped
@Stateless
public class PersonaDAOImpl implements IPersonaDAO,Serializable {
	
	//private EntityManagerFactory emf;
	@PersistenceContext(unitName="PersonalPU")
	private EntityManager em;
	private List<Persona> lstPersonas;
	private int cont;
	
	

	public PersonaDAOImpl() {
		//emf=Persistence.createEntityManagerFactory("PersonalPU");
		//em=emf.createEntityManager();
		lstPersonas=new ArrayList<>();
		cont=0;
		
	}
	

	public void registrar(Persona per,String url)  throws Exception{
		// TODO Auto-generated method stub
		System.out.println("[CDI]Se regisro : "+per.getNombre());
		this.mostrarExcel(url);
		try {
			
			for(Persona per3 :lstPersonas)
			  {
			//em.getTransaction().begin();
	           if(cont==0) {
			      em.persist(per3);//PARA INSERT.....MERGE ES PARA ACTUALIZAR
			      //em.getTransaction().commit();
	           }else {
				   em.merge(per3);
			    }
	           cont++;
		
			 }
			
		}catch(PersistenceException e) {
			/*if(em.getTransaction().isActive()) 
			{  
				em.getTransaction().rollback();
			}*/
			System.out.println("PersonaDAOImpl aquiiiii");
			System.out.println(e.getMessage()+"    "+e.getCause());
			System.out.println(e.getLocalizedMessage());
			
		}finally {
		
		}
	}

	public void modificar(Persona per) throws Exception {
		// TODO Auto-generated method stub
		em.merge(per);
		
	}

	public List<Persona> listar() throws Exception 
	{
		List<Persona> lista=new ArrayList<>();
		Query q=em.createQuery("FROM Persona p order by fecha asc");
		lista=(List<Persona>) q.getResultList();
		return lista;
	}
	
	public void mostrarExcel(String urll) throws IOException, EncryptedDocumentException, InvalidFormatException {
		Date fechaSeleccionada;
		//paso 0. Definir una colecci�n con nombres de las columnas a procesar
		//considera que esto lo puedes leer de un archivo de configuraci�n,
		//input de usuario o cualquier otra fuente
		List<String> columnas = Arrays.asList("MONEDA","DEPENDENCIA","CONCEP","NUMERO","CODIGO","NOMBRE","IMPORTE","FECHA");
		//paso 1.
		Map<String,Integer> mapNombresColumnas = new TreeMap<String,Integer>();
		//paso 2.
		//n�mero de fila donde est�n los nombres de celda
		//recuerda que POI est� basado con �ndice 0
		//si tus nombres est�n en la fila 1, entonces deber�as iniciar esta
		//variable con 0.
		final int filaNombresColumnas =0;
		//url representa el nombre del archivo excel a subir
		//String url2=urll.toString();
		//System.out.println(url2.substring(16,18));
		//System.out.println(url2.substring(19,21));
		//System.out.println(url2.substring(22,24));
		
		File archivoExcel = new File("D:/carga/"+urll);
		//abrir el archivo con POI
		Workbook workbook = WorkbookFactory.create(archivoExcel);
		//ubicarse en la hoja donde vas a procesar
		//si es la primera hoja, debes indicar 0
		HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(0);
		//acceder a la fila con los nombres de las columnas
		Row row = (  sheet).getRow(filaNombresColumnas);
		//paso 3.
		//utilizando el poder de Java 8
		row.cellIterator().forEachRemaining(cell -> {
		    //paso 3.1.
		    String valorCelda = cell.getStringCellValue().trim();
		    
		    //Persona per=new Persona();
		    //per.setMoneda(cell.getStringCellValue().trim());
		    //per.setDependencia(cell.getStringCellValue().trim());
		    if (!valorCelda.isEmpty()) 
		    {
		        mapNombresColumnas.put(valorCelda, cell.getColumnIndex());
		    }
		});
		//paso 4.
		//se asume que los valores para procesar se encuentran en la fila
		//siguiente a la fila donde est�n los nombres de las columnas
		int indiceDatos = filaNombresColumnas + 1;
		Row filaDatos = null;
		List<Persona> lstPersonas1=new ArrayList<>();
		//recorrer todas las filas con datos
		while ((filaDatos = ((org.apache.poi.ss.usermodel.Sheet) sheet).getRow(indiceDatos++)) != null) {
		    //se procesan solo las celdas en base a los "nombres" de esas columnas
		       //el resultado de mapNombresColumnas.get(col) es
		       //el n�mero de columna a leer
		       //en este caso, solo se imprime el resultado
		       //puedes reemplazar esto por la manera en que debas procesar la informaci�n
			 
			   Persona pers=new Persona();
			   LocalDate ff;
			 if(!((filaDatos.getCell(mapNombresColumnas.get("MONEDA"))).toString()).equalsIgnoreCase("")) {  
			   pers.setUrl(urll);
			   String monedaa=(((filaDatos.getCell(mapNombresColumnas.get("MONEDA"))).toString()) );
			  // System.out.println("***"+monedaa);
			   pers.setMoneda(monedaa.substring(0,3));
			   //System.out.println(monedaa.substring(0,3));
			   pers.setDependencia(filaDatos.getCell(mapNombresColumnas.get("DEPENDENCIA"))+"");
			   String concep=(filaDatos.getCell(mapNombresColumnas.get("CONCEP"))+"");
			   //System.out.println(concep);
			   pers.setConcepto(concep.substring(0,6)); 
			   String nume=filaDatos.getCell(mapNombresColumnas.get("NUMERO"))+"";
               String numeroo="";
			   
			   if(nume.substring(1,2).equals(".") && nume.length()==3) {
				   
			   }
			   else if(numeroo.length()==9)
                      {   
				       numeroo=nume.substring(0,7);
				      }
				   else  if(nume.length()==11)
                         {   
				           numeroo=nume.substring(0,1)+nume.substring(2,9);
				         }
				         else if(nume.length()==10) 
				              {
				        	    numeroo=nume.substring(0,1)+nume.substring(2,8);
				              }
				              else if(nume.length()==7) 
				                   {
				            	    numeroo=nume.substring(0,5);
				                   }
				                  else if(nume.length()==8) {
				                	  numeroo=nume.substring(0,6);
				                  }
			   pers.setNumero(numeroo);
			   String codi=filaDatos.getCell(mapNombresColumnas.get("CODIGO"))+"";
			   String codigoo="";
			   
			   if(codi.substring(1,2).equals(".")&&codi.length()==3) {
			
				  // pers.setCodigo();
			   }
			   else if(codi.length()==9)
                      {   
				       codigoo=codi.substring(0,7); 
				      }
				   else  if(codi.length()==11)
                         {   
				           codigoo=codi.substring(0,1)+codi.substring(2,9);
				         }
				         else if(codi.length()==10) 
				              {
				        	   codigoo=codi.substring(0,1)+codi.substring(2,8);
				              }
				              else if(codi.length()==7) 
				                   {
				            	    codigoo=codi.substring(0,5);
				                   }
				                  else if(codi.length()==8) {
				                	  codigoo=codi.substring(0,6);
				                  }

			   pers.setCodigo(codigoo);
			   
			   pers.setNombre(filaDatos.getCell(mapNombresColumnas.get("NOMBRE"))+"");
			   pers.setImporte(  Double.parseDouble((filaDatos.getCell(mapNombresColumnas.get("IMPORTE")).toString())));
			   String fechaa=urll;
			   
               ff=LocalDate.of(Integer.parseInt("20"+fechaa.substring(22,24)),Integer.parseInt(fechaa.substring(19,21)),Integer.parseInt((fechaa.substring(16,18))));
			  
			   pers.setFecha(ff);
		       //System.out.println(pers.getId()+"AAA/"+pers.getMoneda()+"/"+pers.getDependencia()+"/"+pers.getConcepto()+"/"+pers.getNumero()+
		    		//   "/"+pers.getCodigo()+"/"+pers.getNombre());	    
			   lstPersonas1.add(pers);
		    }
		  }	 
		
		   this.lstPersonas=lstPersonas1;
		}

	
	@Override
	public Persona ListarPorId(Persona t) throws Exception {
		List<Persona> lista=new ArrayList<>();
		Query q=em.createQuery("FROM Persona p where p.id = ?");
		q.setParameter(1,t.getId());
		lista=(List<Persona>) q.getResultList();
		
		Persona per=lista != null && !lista.isEmpty() ? lista.get(0) : new Persona();
	
		return per;
	  }


	@Override
	public List<Persona> listarxPersona(Persona t,LocalDate inicio,LocalDate fin) throws Exception 
	 { /* There are two approaches to parameter binding: using positional or using
		named parameters. Hibernate and Java Persistence support both options, but you
		can�t use both at the same time for a particular query.
		With named parameters, you can rewrite the query as
		String queryString =
		"from Item item where item.description like :search";
		*/
		
		List<Persona> lista =new ArrayList<>();
		Query q=em.createQuery("From Persona p where (p.nombre LIKE  :code) AND ( p.nombre LIKE :code1)"
				+ " AND (p.fecha BETWEEN :startDate AND :endDate)");
		//Query q=em.createQuery("From Persona p where p.id = 363 ");
		q.setParameter("code","%"+t.getNombre()+"%");
		q.setParameter("code1","%"+t.getDependencia()+"%");
		q.setParameter("startDate",inicio);
		q.setParameter("endDate",fin);
		
		System.out.println("'%"+t.getNombre()+"%'");
		lista=(List<Persona>) q.getResultList();
		//Persona p2=lista.get(0);
		//System.out.println(p2.getNombre()+" / "+p2.getDependencia());
		return lista;
	  }
	
	}

	

