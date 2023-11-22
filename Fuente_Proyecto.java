import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class Fuente_Proyecto extends JFrame implements ActionListener, KeyListener {

     private static final int TAMANO_CUADRO = 40; // El tamano de cada cuadro en pixeles
       private static final int TAMANO_TABLERO = 8;  // El tamano del tablero en cuadros


    private static final int VELOCIDAD_JUEGO = 100; // Velocidad del juego en milisegundos



    // Direcciones para mover la serpiente
    private enum Direccion { ARRIBA, ABAJO, IZQUIERDA, DERECHA }



          private LinkedList<Point> serpiente; // Lista que almacena las posiciones de la serpiente
      private Direccion direccion;         // La direccion actual de movimiento de la serpiente
     private Point comida;                // La posicion de la comida en el tablero
          private Timer temporizador;          // Temporizador para manejar el movimiento "dejando sola" la serpiente

    // Constructor de la clase
    public Fuente_Proyecto() {
        setTitle("SSerpiente"); //  titulo de la ventana


                 setSize(TAMANO_CUADRO * TAMANO_TABLERO, TAMANO_CUADRO * TAMANO_TABLERO);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setLocationRelativeTo(null);

                serpiente = new LinkedList<>(); // Inicializar la lista de la serpiente
        direccion = Direccion.DERECHA;   // Inicializar la direccion de la serpiente

          inicializarSerpiente(); // Inicializar la posicion inicial de la serpiente


                spawnComida();          // Colocar la comida en una posicion aleatoria


        temporizador = new Timer(VELOCIDAD_JUEGO, this); // Configurar el temporizador
                             temporizador.start(); // Iniciar el temporizador

        // Configurar el escuchador de teclas para manejar la entrada del usuario
        addKeyListener(new KeyAdapter() {
            @Override
                        public void keyPressed(KeyEvent e) {

                handleKeyPress(e);
            }



        });

        setFocusable(true); // Permitir que la ventana tenga el foco para recibir eventos de teclado
    }

    // Inicializa la serpiente con una posicion inicial
    private void inicializarSerpiente() {
                           serpiente.add(new Point(3, 3));
    }

    // Coloca la comida en una posicion aleatoria que no este ocupada por la serpiente
    private void spawnComida() {

        Random random = new Random();
                         int x, y;
        do {
                     x = random.nextInt(TAMANO_TABLERO);

            y = random.nextInt(TAMANO_TABLERO);
        } while (serpiente.contains(new Point(x, y)));
                              comida = new Point(x, y);
    }

    // Maneja el movimiento de la serpiente
    private void mover() {

        Point cabeza = serpiente.getFirst(); // Obtener la posicion de la cabeza de la serpiente
                           Point nuevaCabeza = obtenerNuevaCabeza(); // Calcular la nueva posicion de la cabeza

         // Verificar si la serpiente ha alcanzado la comida
                         if (nuevaCabeza.equals(comida)) {

                             serpiente.addFirst(comida); // Anadir la comida a la serpiente
            spawnComida();              // Colocar nueva comida en una posicion aleatoria
        } else {
               serpiente.addFirst(nuevaCabeza); // Mover la serpiente hacia la nueva posicion
            serpiente.removeLast();          // Eliminar la ultima posicion, ya que la serpiente se movio
        }

        checkColision(); // Verificar si la serpiente choca con las paredes o consigo misma
        repaint();       // Volver a dibujar la ventana para mostrar los cambios
    }

    // Calcula la nueva posicion de la cabeza de la serpiente en funcion de la direccion actual
    private Point obtenerNuevaCabeza() {
                                      Point cabeza = serpiente.getFirst(); // Obtener la posicion actual de la cabeza

        // Calcular la nueva posicion en funcion de la direccion actual
        switch (direccion) {

            case ARRIBA:    return new Point(cabeza.x, (cabeza.y - 1 + TAMANO_TABLERO) % TAMANO_TABLERO);
                    case ABAJO:     return new Point(cabeza.x, (cabeza.y + 1) % TAMANO_TABLERO);
            case IZQUIERDA: return new Point((cabeza.x - 1 + TAMANO_TABLERO) % TAMANO_TABLERO, cabeza.y);
                      case DERECHA:   return new Point((cabeza.x + 1) % TAMANO_TABLERO, cabeza.y);

                      default: throw new IllegalStateException("Valor inesperado: " + direccion);
        }
    }

    // Verifica si la serpiente ha chocado con las paredes o consigo misma
    private void checkColision() {
                       Point cabeza = serpiente.getFirst(); // Obtener la posicion de la cabeza

        // Verificar colision con las paredes del tablero
                     if (cabeza.x < 0 || cabeza.x >= TAMANO_TABLERO || cabeza.y < 0 || cabeza.y >= TAMANO_TABLERO) {

                         finDelJuego(); // Si hay colision, terminar el juego

        }

        // Verificar colision de la cabeza con el cuerpo de la serpiente
        if (serpiente.size() > 1 && serpiente.subList(1, serpiente.size()).contains(cabeza)) {
            finDelJuego(); // Si hay colision, terminar el juego
        }
    }

    // Muestra un mensaje de fin de juego y cierra la aplicacion
    private void finDelJuego() {
                         temporizador.stop(); // Detener el temporizador

        JOptionPane.showMessageDialog(this, "¡Buen Juego!\nPuntuacion: " + (serpiente.size() - 1),
                "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0); // Cerrar la aplicacion
    }

    // Metodo que se llama cuando se necesita repintar la ventana
    @Override
        public void paint(Graphics g) {

              super.paint(g);


              drawGrid(g);       // Dibujar el tablero
        drawSerpiente(g);  // Dibujar la serpiente

        drawComida(g);     // Dibujar la comida
    }

    // Dibuja el tablero del juego
    private void drawGrid(Graphics g) {
                 for (int i = 0; i < TAMANO_TABLERO; i++) {

                     for (int j = 0; j < TAMANO_TABLERO; j++) {

                         g.drawRect(i * TAMANO_CUADRO, j * TAMANO_CUADRO, TAMANO_CUADRO, TAMANO_CUADRO);
            }
        }
    }

    // Dibuja la serpiente en la ventana
    private void drawSerpiente(Graphics g) {
                g.setColor(Color.ORANGE);


                for (Point point : serpiente) {
            g.fillRect(point.x * TAMANO_CUADRO, point.y * TAMANO_CUADRO, TAMANO_CUADRO, TAMANO_CUADRO);
        }
    }

    // Dibuja la comida en la ventana
    private void drawComida(Graphics g) {


                                       g.setColor(Color.BLUE);
        g.fillRect(comida.x * TAMANO_CUADRO, comida.y * TAMANO_CUADRO, TAMANO_CUADRO, TAMANO_CUADRO);
    }

    // Maneja el evento de pulsacion de tecla
    private void handleKeyPress(KeyEvent e) {
                int keyCode = e.getKeyCode();

        // Cambia la direccion de la serpiente segun la tecla presionada
        switch (keyCode) {
                         case KeyEvent.VK_UP:

                if (direccion != Direccion.ABAJO) direccion = Direccion.ARRIBA;
                break;
                                   case KeyEvent.VK_DOWN:
                if (direccion != Direccion.ARRIBA) direccion = Direccion.ABAJO;

                          break;


             case KeyEvent.VK_LEFT:
                if (direccion != Direccion.DERECHA) direccion = Direccion.IZQUIERDA;
                           break;
            case KeyEvent.VK_RIGHT:
                if (direccion != Direccion.IZQUIERDA) direccion = Direccion.DERECHA;
                break;
        }
    }

    // Metodo llamado en cada tick del temporizador para mover la serpiente
    @Override
    public void actionPerformed(ActionEvent e) {
                   mover();
    }

    // Metodos de la interfaz KeyListener (no utilizados en esta implementacion)
    @Override


    public void keyTyped(KeyEvent e) {}

    @Override
            public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    // Metodo principal que inicia la aplicacion
    public static void main(String[] args) {
                   SwingUtilities.invokeLater(() -> {

                       new Fuente_Proyecto().setVisible(true);
        });
    }
}
// Olvide decir que la serpiente se controla con las flechas del teclado :D
