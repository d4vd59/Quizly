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
            _main.ShowBottomBar(true);  // BottomBar in HomeView anzeigen
        }

        private void NewGame_Click(object sender, RoutedEventArgs e)
        {
            // Neues Spiel starten → Spielmodus-Auswahl
            _main.ShowBottomBar(false);
            _main.NavigateTo(new GameModeSelectionView(_main));
        }

        private void Play_Click(object sender, RoutedEventArgs e)
        {
            // Spiel spielen → DuelOverviewView
            _main.ShowBottomBar(false);
            _main.NavigateTo(new DuelOverviewView(_main));
        }

        private void Game_Click(object sender, MouseButtonEventArgs e)
        {
            // Auf Spiel-Card klicken → DuelOverviewView
            _main.ShowBottomBar(false);
            _main.NavigateTo(new DuelOverviewView(_main));
        }

        private void ViewGame_Click(object sender, MouseButtonEventArgs e)
        {
            // Spiel ansehen (Gegner spielt) → DuelOverviewView
            _main.ShowBottomBar(false);
            _main.NavigateTo(new DuelOverviewView(_main));
        }

        private void ViewResult_Click(object sender, RoutedEventArgs e)
        {
            // Ergebnis ansehen → ResultView
            _main.ShowBottomBar(false);
            _main.NavigateTo(new ResultView(_main));
        }

        private void Settings_Click(object sender, RoutedEventArgs e)
        {
            // Settings Popup anzeigen
            SettingsPopup.Visibility = Visibility.Visible;
        }

        private void ClosePopup_Click(object sender, RoutedEventArgs e)
        {
            // Popup schließen - KORRIGIERT: RoutedEventArgs statt MouseButtonEventArgs
            SettingsPopup.Visibility = Visibility.Collapsed;
        }

        private void PreventClose_Click(object sender, MouseButtonEventArgs e)
        {
            // Verhindert, dass Popup schließt wenn man auf inneren Bereich klickt
            e.Handled = true;
        }

        private void Logout_Click(object sender, RoutedEventArgs e)
        {
            // Ausloggen und zur LoginView navigieren
            SettingsPopup.Visibility = Visibility.Collapsed;
            _main.GoLogin();
        }
    }
}