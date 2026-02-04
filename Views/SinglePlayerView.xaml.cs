using System.Windows.Controls;

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

            UpdateDisplay();
        }

        private void UpdateDisplay()
        {
            var gameState = _main.CurrentGameState;

            // Score aktualisieren (Spieler Punkte / Maximum 18 Punkte)
            FinalScore.Text = $"{gameState.PlayerScore} / 18";

            // Runden-Status aktualisieren basierend auf CurrentRound
            UpdateRoundDisplay(gameState.CurrentRound);
        }

        private void UpdateRoundDisplay(int currentRound)
        {
            // Hier können Sie die Runden visuell aktualisieren
            // Beispiel: Runde 1 aktivieren wenn currentRound >= 1
            if (currentRound >= 1)
            {
                R1Border.Opacity = 1.0;
                R1Border.Background = (System.Windows.Media.Brush)FindResource("CardStrong");
            }
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