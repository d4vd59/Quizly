using System.Windows.Controls;


namespace Quizly.Views
{
    public partial class QuestionView : UserControl
    {
        private readonly MainWindow _main;

        public QuestionView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private void Back_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new CategoryPickView(_main));
        }

        private void Answer_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            // Runde abschließen
            _main.CurrentGameState.CurrentRound++;

            // Beispiel: Score aktualisieren (hier sollten Sie die tatsächliche Logik implementieren)
            // _main.CurrentGameState.PlayerScore += 1;

            // ✅ NEU: Prüfen ob Singleplayer oder Multiplayer
            if (_main.CurrentGameState.IsGameFinished())
            {
                // Spiel ist zu Ende
                _main.NavigateTo(new ResultView(_main));
            }
            else
            {
                // ✅ GEÄNDERT: Zur richtigen Overview navigieren
                if (_main.CurrentGameState.IsSinglePlayer)
                {
                    // Singleplayer → zurück zu SinglePlayerView
                    _main.NavigateTo(new SinglePlayerView(_main));
                }
                else
                {
                    // Multiplayer → zurück zu DuelOverviewView
                    _main.NavigateTo(new DuelOverviewView(_main));
                }
            }
        }
    }
}