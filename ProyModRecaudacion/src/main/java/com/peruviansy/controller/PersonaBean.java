package com.peruviansy.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.peruviansy.model.Persona;
import com.peruviansy.service.IPersonaService;
import com.peruviansy.service.Impl.PersonaServiceImpl;

@Named
@ViewScoped
public class PersonaBean implements Serializable{
	
	private List<Persona> lstPersonas=new ArrayList<Persona>();
	private String url=new String();
	private String url2=new String();
	private List<Persona> lstReporte=new ArrayList<Persona>();
	private String nombre;
	private String apellido;
	private double monto;
	private int nro;
	private File file2;
	private Date fechainicio;
	private Date fechafinal;
	private LocalDate fechainicio1;
	private LocalDate fechafinal1;
	
	@Inject
	private Persona persona;
	
	@Inject
	private IPersonaService service;
	private UploadedFile file;

	
	@PostConstruct
	public void init() 
	{     this.lstReporte.clear();
	      nombre=new String();
	      this.apellido=new String();
	    	this.listar();
	}
	  
    public void listar()
    {
    	try {
    		lstPersonas=service.listar();
    		
    	}catch(Exception e) {
    		
    	}
    }
    
    public void listarxId() {
    	try 
    	{
    		//lstPersonas=service.ListarPorId(this.persona);
    	}catch(Exception e) {
    		
    	}
    }

    
    
    
	public String getUrl2() {
		return url2;
	}

	public void setUrl2(String url2) {
		this.url2 = url2;
	}

	public Date getFechainicio() {
		return fechainicio;
	}

	public void setFechainicio(Date fechainicio) {
		this.fechainicio = fechainicio;
	}

	public Date getFechafinal() {
		return fechafinal;
	}

	public void setFechafinal(Date fechafinal) {
		this.fechafinal = fechafinal;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public int getNro() {
		return nro;
	}

	public void setNro(int nro) {
		this.nro = nro;
	}
	
	public List<Persona> getLstReporte() {
		return lstReporte;
	}

	public void setLstReporte(List<Persona> lstReporte) {
		this.lstReporte = lstReporte;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public List<Persona> getLstPersonas() {
		return lstPersonas;
	}

	public void setLstPersonas(List<Persona> lstPersonas) {
		this.lstPersonas = lstPersonas;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public UploadedFile getFile() {
		return file;
	}
	
	public void setFile(UploadedFile file) {
		this.file = file;
	}
	
	  public void upload() {
	        if(file != null) {
	            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
	            FacesContext.getCurrentInstance().addMessage(null, message);
	        }
	        this.url=file.getFileName();
	    }
	
	 public void convertirMayusculas()
	  {   this.apellido=this.apellido.toUpperCase();
		  
	  }
	  
	  public void convertirNombreMayusculas() {
			// TODO Auto-generated method stub
		  this.nombre=this.nombre.toUpperCase();
		}
	  
	  public void myFileUpload() throws IOException {
		 
		  File file2 = new File(file.getFileName());  
	      // System.out.println(file2.getCanonicalPath()); 
	      //System.out.println(file2.getPath()); 
	      //System.out.println(file2.getAbsoluteFile()); 
		  
		  this.url=this.file.getFileName()  ;
		  this.url2=this.url;
		  //ServletContext servletContext=(ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
		 // this.url2=servletContext.getRealPath("")+File.separator+"upload"+File.separator+this.url;
		  FacesMessage msg=new FacesMessage("Succesful"+this.url+" is Uploaded");
		  FacesContext.getCurrentInstance().addMessage(null,msg);
		  //System.out.println(this.url2);
		  registrar();
		  
	  }
	  
	  public void myFileUploadMasivo() throws IOException, EncryptedDocumentException, InvalidFormatException 
	  {
		  String diaa=""; 
		  //File file2 = new File(file.getFileName());  
		  String urll=file.getFileName();
		  
		  boolean band=true;
		  int mes=Integer.parseInt(urll.substring(19,21));
		  int anio=Integer.parseInt(urll.substring(22,24));
		  int dia=1;
		while(dia<31 && band) {  
		 
		  if(dia<10) 
		  {
		     diaa="0"+String.valueOf(dia);
		  }
		  else 
		  {
			   diaa=String.valueOf(dia);
		  }
		  String fecha=diaa+"-"+urll.substring(19,21)+"-"+urll.substring(22,24)+".xls";
		  int mess=Integer.parseInt(urll.substring(19,21));
		  
		  System.out.println(urll.substring(19,21));//mes
		  System.out.println(urll.substring(22,24));//anio
		  
		  File archivoExcel = new File("D:/carga/"+"Sistemas_recaud."+fecha);
		  //abrir el archivo con POI
		  if(archivoExcel.exists()) 
		  {
			 /* Workbook workbook = WorkbookFactory.create(archivoExcel);
			  HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(0);
			  Row row = (  sheet).getRow(0);
			  String valor=row.getCell(0).toString();
			  String valor1=row.getCell(1).toString();
			  //System.out.println(valor);
			  //System.out.println(valor1);*/
			  this.url2="Sistemas_recaud."+fecha;
			  System.out.println("D:/carga/"+"Sistemas_recaud."+fecha);
			  FacesMessage msg=new FacesMessage("Archivo cargado "+this.url2);
			  FacesContext.getCurrentInstance().addMessage(null,msg);
			  //System.out.println(this.url2);
			  registrar();	
		  }
		  
		  
		   if(mess==1||mess==3||mess==5||mess==7||mess==8||mess==10||mess==12)
		   {   if(dia<=31)
		         dia++;
		       else
		    	 band=false;
		   }
		   else if(mess==2){
			      if(dia<=28) {
			         dia++;
			      }else {
			    	  band=false;
			           }
		        }else
		        {
		        	if(dia<=30)
		        	{
		        	   dia++;
		        	}
		        	else 
		        	 {
		        		band=false;
		        	}
		          }
	      } 
		
	  }
	  

	public void registrar() 
	{
		try {
			
		//Calendar cal=Calendar.getInstance();
		//cal.setTime(fechaSeleccionada);
		
		//LocalDate localDate=LocalDate.of(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		
		Persona per=new Persona();
		per.setNombre("Henry Vargas");
		System.out.println("wwwwwww"+this.url2);
		
		service.registrar(per,this.url2);
		
	    }catch(Exception e) 
		{
		e.printStackTrace();
	    }

   }
	
	public void listarxPersona() {
		try {
		  this.monto=0;
		  Persona per=new Persona();
		  System.out.println("******fechainicio*"+fechainicio);
		  if(!this.apellido.equalsIgnoreCase("") && apellido!=null)
	        this.convertirMayusculas();
		  if(!this.nombre.equalsIgnoreCase("") && nombre!=null)
		        this.convertirNombreMayusculas();
		  
	      per.setNombre(this.nombre);
		  per.setDependencia(this.apellido);
		  System.out.println("******fechainicio*"+fechainicio);
		  Calendar cal= Calendar.getInstance();
		  if(fechainicio!=null) 
		  {
		     cal.setTime(fechainicio);
		     fechainicio1=LocalDate.of(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH));
		  }
		  else {
			  fechainicio1=null;
		  }
		  Calendar cal1=Calendar.getInstance(); 
		  
		  if(fechafinal!=null)
		  {
			  cal1.setTime(fechafinal);
			  fechafinal1=LocalDate.of(cal1.get(Calendar.YEAR),cal1.get(Calendar.MONTH)+1,cal1.get(Calendar.DAY_OF_MONTH));
		  }
		  else 
		  {
			  fechafinal1=null;
		  }
		  List<Persona> lst=new ArrayList<Persona>();
		  this.lstReporte=lst;
		  this.lstReporte= service.listarxPersona(per,fechainicio1,this.fechafinal1);
	  
		}catch(Exception e) 
		  {
			System.out.println(e.getMessage());
		  }
	}
}
