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

            // Prüfen ob alle 6 Runden gespielt wurden
            if (_main.CurrentGameState.IsGameFinished())
            {
                _main.NavigateTo(new ResultView(_main));
            }
            else
            {
                _main.NavigateTo(new DuelOverviewView(_main));
            }
        }
    }
}