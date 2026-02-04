using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;

namespace Quizly.Views
{
    public partial class HomeView : UserControl
    {
        private readonly MainWindow _main;

        public HomeView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private void NewGame_Click(object sender, RoutedEventArgs e)
        {
            // Neues Spiel starten → MatchmakingView
            _main.NavigateTo(new MatchmakingView(_main));
        }

        private void Play_Click(object sender, RoutedEventArgs e)
        {
            // Spiel spielen → DuelOverviewView
            _main.NavigateTo(new DuelOverviewView(_main));
        }

        private void Game_Click(object sender, MouseButtonEventArgs e)
        {
            // Auf Spiel-Card klicken → DuelOverviewView
            _main.NavigateTo(new DuelOverviewView(_main));
        }

        private void ViewGame_Click(object sender, MouseButtonEventArgs e)
        {
            // Spiel ansehen (Gegner spielt) → DuelOverviewView
            _main.NavigateTo(new DuelOverviewView(_main));
        }

        private void ViewResult_Click(object sender, RoutedEventArgs e)
        {
            // Ergebnis ansehen → ResultView
            _main.NavigateTo(new ResultView(_main));
        }
    }
}