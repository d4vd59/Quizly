using System.Windows.Controls;
using System.Windows.Media;

namespace Quizly.Views
{
    public partial class SinglePlayerView : UserControl
    {
        private readonly MainWindow _main;

        public SinglePlayerView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
            _main.ShowBottomBar(false);  // BottomBar ausblenden

            // ✅ Einfache GUI-Initialisierung
            UpdateDisplay();
        }

        private void UpdateDisplay()
        {
            var gameState = _main.CurrentGameState;

            // Score anzeigen (z.B. "3 / 18")
            FinalScore.Text = $"{gameState.PlayerScore} / 18";

            // Optional: Status-Text aktualisieren
            StatusText.Text = gameState.CurrentRound == 0 
                ? "Singleplayer Training" 
                : $"Runde {gameState.CurrentRound} abgeschlossen!";
        }

        private void Back_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.GoHome();
        }

        private void Play_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            // Zur Kategorieauswahl navigieren
            _main.NavigateTo(new CategoryPickView(_main));
        }
    }
}