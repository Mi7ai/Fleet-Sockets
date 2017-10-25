package cliente;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.*;

import partida.Partida;

public class ClienteFlotaSockets {

	// Sustituye esta clase por tu versión de la clase Juego de la práctica 1

	// Modifícala para que instancie un objeto de la clase AuxiliarClienteFlota en el método 'ejecuta'

	// Modifica todas las llamadas al objeto de la clase Partida
	// por llamadas al objeto de la clase AuxiliarClienteFlota.
	// Los métodos a llamar tendrán la misma signatura.

	/**
	 * Implementa el juego 'Hundir la flota' mediante una interfaz gráfica (GUI)
	 */

	/** Parametros por defecto de una partida */
	public static final int NUMFILAS=8, NUMCOLUMNAS=8, NUMBARCOS=6;

	private GuiTablero guiTablero = null;			// El juego se encarga de crear y modificar la interfaz gráfica
	private AuxiliarClienteFlota auxClienteFlota = null;// Objeto con los datos de la partida en juego

	/** Atributos de la partida guardados en el juego para simplificar su implementación */
	private int quedan = NUMBARCOS, disparos = 0;

	/**
	 * Programa principal. Crea y lanza un nuevo juego
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 */
	public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
		ClienteFlotaSockets juego = new ClienteFlotaSockets();
		juego.ejecuta();
	} // end main

	/**
	 * Lanza una nueva hebra que crea la primera partida y dibuja la interfaz grafica: tablero
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 */
	private void ejecuta() throws SocketException, UnknownHostException, IOException {
		// Instancia la primera partida
		auxClienteFlota = new AuxiliarClienteFlota("localhost","12345");
		auxClienteFlota.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				guiTablero = new GuiTablero(NUMFILAS, NUMCOLUMNAS);
				guiTablero.dibujaTablero();
			}
		});
	} // end ejecuta

	/******************************************************************************************/
	/*********************  CLASE INTERNA GuiTablero   ****************************************/
	/******************************************************************************************/
	private class GuiTablero {

		private int numFilas, numColumnas;

		private JFrame frame = null;        // Tablero de juego
		private JLabel estado = null;       // Texto en el panel de estado
		private JButton buttons[][] = null; // Botones asociados a las casillas de la partida

		/**
		 * Constructor de una tablero dadas sus dimensiones
		 */
		GuiTablero(int numFilas, int numColumnas) {
			this.numFilas = numFilas;
			this.numColumnas = numColumnas;
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		}

		/**
		 * Dibuja el tablero de juego y crea la partida inicial
		 */
		public void dibujaTablero() {
			anyadeMenu();
			anyadeGrid(numFilas, numColumnas);		
			anyadePanelEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);		
			frame.setSize(300, 300);
			frame.setVisible(true);	
		} // end dibujaTablero

		/**
		 * Anyade el menu de opciones del juego y le asocia un escuchador
		 */
		private void anyadeMenu() {
			// POR IMPLEMENTAR
			//create menu bar
			final JMenuBar menuBar = new JMenuBar();

			//create menu
			JMenu opcionesMenu = new JMenu("Opciones");

			//create menu items
			JMenuItem nuevaPartida = new JMenuItem("Nueva Partida");
			nuevaPartida.setActionCommand("Nueva Partida");
			JMenuItem mostrarSolucion = new JMenuItem("Mostrar Solucion");
			mostrarSolucion.setActionCommand("Mostrar Solucion");
			JMenuItem salir = new JMenuItem("Salir");
			salir.setActionCommand("Salir");

			//create menu item listener and links 
			MenuListener menuItemListener = new MenuListener();
			nuevaPartida.addActionListener(menuItemListener);
			mostrarSolucion.addActionListener(menuItemListener);
			salir.addActionListener(menuItemListener);

			//add items to menu
			opcionesMenu.add(nuevaPartida);
			opcionesMenu.add(mostrarSolucion);
			opcionesMenu.add(salir);

			//add menu to menubar
			menuBar.add(opcionesMenu);
			//add menubar to the frame
			frame.setJMenuBar(menuBar);
			frame.setVisible(true);

		} // end anyadeMenu

		/**
		 * Anyade el panel con las casillas del mar y sus etiquetas.
		 * Cada casilla sera un boton con su correspondiente escuchador
		 * @param nf	numero de filas
		 * @param nc	numero de columnas
		 */
		private void anyadeGrid(int nf, int nc) {
			// POR IMPLEMENTAR

			//create game table panel
			JPanel tablero = new JPanel(new GridLayout(nf, nc));
			JPanel panelNorth = new JPanel(new GridLayout(1, nc));
			JPanel panelEast = new JPanel(new GridLayout(nf, 1));
			JPanel panelWest = new JPanel(new GridLayout(nf, 1));
			buttons = new JButton[nf][nc];

			ButtonListener tableButtonListener = new ButtonListener();
			//--------------------------------------------------
			//North Panel
			panelNorth.add(new Label(" "));
			for (int i = 1; i <= nf; i++) {
				panelNorth.add(new Label(" "+ i));
			}
			//West Panel
			for (int i = 0; i < nc; i++) {
				panelWest.add(new Label(" "+ Character.toString((char) ('A' + i))));
			}
			//East Panel
			for (int i = 0; i < nc; i++) {
				panelEast.add(new Label(" "+ Character.toString((char) ('A' + i))));
			}

			//--------------------------------------------------


			//initialize buttons vector
			for (int i = 0; i < nf; i++) {
				for (int j = 0; j < nc; j++) {
					buttons[i][j]=new JButton();					
					buttons[i][j].putClientProperty("fila",i);
					buttons[i][j].putClientProperty("columna",j);					 
				}
			} 
			//add buttons to table 
			for (int i = 0; i < buttons.length; i++) {
				for (int j = 0; j < buttons.length; j++) {
					buttons[i][j].addActionListener(tableButtonListener);//a�adir evento a cada boton
					tablero.add(buttons[i][j]);//a�adir boton al tablero
				}
			}

			//add the table panel to main frame,centered
			frame.add(tablero,BorderLayout.CENTER, SwingConstants.CENTER);
			frame.add(panelNorth,BorderLayout.NORTH);
			frame.add(panelEast,BorderLayout.EAST);
			frame.add(panelWest,BorderLayout.WEST);

		} // end anyadeGrid


		/**
		 * Anyade el panel de estado al tablero
		 * @param cadena	cadena inicial del panel de estado
		 */
		private void anyadePanelEstado(String cadena) {	
			JPanel panelEstado = new JPanel();
			estado = new JLabel(cadena);
			panelEstado.add(estado);
			// El panel de estado queda en la posición SOUTH del frame
			frame.getContentPane().add(panelEstado, BorderLayout.SOUTH);
		} // end anyadePanel Estado

		/**
		 * Cambia la cadena mostrada en el panel de estado
		 * @param cadenaEstado	nuevo estado
		 */
		public void cambiaEstado(String cadenaEstado) {
			estado.setText(cadenaEstado);
		} // end cambiaEstado

		/**
		 * Muestra la solucion de la partida y marca la partida como finalizada
		 * @throws IOException 
		 */
		public void muestraSolucion() throws IOException {
			for(int i = 0;i<NUMFILAS ; i++) {
				for(int j=0; j<NUMCOLUMNAS ;j++) {
					int color = auxClienteFlota.pruebaCasilla(i, j);//devuelve color casilla        			

					if(color==-1){       					
						guiTablero.pintaBoton(buttons[i][j], Color.cyan);
					}			

				}
			}

			guiTablero.cambiaEstado("LA SOLUCION ES : ");
			for(String cadenaBarco : auxClienteFlota.getSolucion()) {
				pintaBarcoHundido(cadenaBarco);
			}
			quedan=0;
		} // end muestraSolucion


		/**
		 * Pinta un barco como hundido en el tablero
		 * @param cadenaBarco	cadena con los datos del barco codifificados como
		 *                      "filaInicial#columnaInicial#orientacion#tamanyo"
		 */
		public void pintaBarcoHundido(String cadenaBarco) {
			// POR IMPLEMENTAR

			String[] partes = cadenaBarco.split("#");
			int fila = Integer.parseInt(partes[0]);

			int col = Integer.parseInt(partes[1]);
			char ori = partes[2].charAt(0);

			int tam = Integer.parseInt(partes[3]);

			switch (ori){
			case 'H':
				for (int i = col; i < col+tam; i++) {
					JButton btn =  buttons[fila][i];
					pintaBoton(btn, Color.red);

				}
				break;
			case 'V':
				for (int i = fila; i < fila+tam; i++) {
					JButton btn =  buttons[i][col];
					pintaBoton(btn, Color.red);

				}
				break;
			}
			JButton btn = null;
			for (int i = 0; i < tam; i++) {
				if (ori=='H') {
					btn = buttons[fila][col+i];
				}else{
					btn = buttons[fila+i][col];

				}
				pintaBoton(btn, Color.red);
			}	 
			quedan--;




		} // end pintaBarcoHundido

		/**
		 * Pinta un botón de un color dado
		 * @param b			boton a pintar
		 * @param color		color a usar
		 */
		public void pintaBoton(JButton b, Color color) {
			b.setBackground(color);
			// El siguiente código solo es necesario en Mac OS X
			b.setOpaque(true);
			b.setBorderPainted(false);
		} // end pintaBoton

		/**
		 * Limpia las casillas del tablero pintándolas del gris por defecto
		 */
		public void limpiaTablero() {
			for (int i = 0; i < numFilas; i++) {
				for (int j = 0; j < numColumnas; j++) {
					buttons[i][j].setBackground(null);
					buttons[i][j].setOpaque(true);
					buttons[i][j].setBorderPainted(true);
				}
			}
		} // end limpiaTablero

		/**
		 * 	Destruye y libera la memoria de todos los componentes del frame
		 */
		public void liberaRecursos() {
			frame.dispose();
		} // end liberaRecursos


	} // end class GuiTablero

	/******************************************************************************************/
	/*********************  CLASE INTERNA MenuListener ****************************************/
	/******************************************************************************************/

	/**
	 * Clase interna que escucha el menu de Opciones del tablero
	 * 
	 */
	private class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// POR IMPLEMENTAR
			switch (e.getActionCommand()) {
			case "Salir":
				guiTablero.liberaRecursos();
				break;
			case "Nueva Partida":
				guiTablero.limpiaTablero();
				
 				//partida= new Partida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);				 
				disparos=0;
				quedan = NUMBARCOS;
				guiTablero.cambiaEstado("Intentos: "+ disparos + " Barcos restantes: "+ quedan);
				try {
        			auxClienteFlota.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
        		}catch (IOException ex) {
        			ex.printStackTrace();
        		}
				for (int i = 0; i < NUMFILAS; i++) {
					for (int j = 0; j < NUMCOLUMNAS; j++) {
						guiTablero.buttons[i][j].setEnabled(true);        				 
						guiTablero.buttons[i][j].removeActionListener(new ButtonListener());
					}
				}
				break;
			case "Mostrar Solucion":
				try {
					guiTablero.muestraSolucion();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;	
			default:
				break;
			}



		} // end actionPerformed

	} // end class MenuListener



	/******************************************************************************************/
	/*********************  CLASE INTERNA ButtonListener **************************************/
	/******************************************************************************************/
	/**
	 * Clase interna que escucha cada uno de los botones del tablero
	 * Para poder identificar el boton que ha generado el evento se pueden usar las propiedades
	 * de los componentes, apoyandose en los metodos putClientProperty y getClientProperty
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// POR IMPLEMENTAR
			try {
				if (quedan > 0) {
					JButton btn = (JButton) e.getSource();//boton pulsado

					int fila =(int) btn.getClientProperty("fila");
					int col =(int) btn.getClientProperty("columna");
					int color = auxClienteFlota.pruebaCasilla(fila, col);//devuelve color casilla

					switch (color) {
					case -1:

						guiTablero.pintaBoton(btn, Color.cyan);
						break;

					case -2:

						guiTablero.pintaBoton(btn, Color.ORANGE);
						break;


					default://si devuelve la id del barco esta hundido
						if(color>=0) {	
							guiTablero.pintaBarcoHundido(auxClienteFlota.getBarco(color));

						}

						break;

					}//end switch

					guiTablero.cambiaEstado("Intentos: "+ ++disparos + " Barcos restantes: "+ quedan);

				}else{
					guiTablero.cambiaEstado("Game over! Acabado en: "+disparos+" intentos");
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		} // end actionPerformed
			
	} // end class ButtonListener
}
