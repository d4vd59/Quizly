using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;


namespace Quizly.Views
{
    /// <summary>
    /// Interaktionslogik für GameModeSelectionView.xaml
    /// </summary>
    public partial class GameModeSelectionView : UserControl
    {
        private readonly MainWindow _main;

        public GameModeSelectionView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private void Back_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.GoHome();
        }

        private void Multiplayer_Click(object sender, MouseButtonEventArgs e)
        {
            // Multiplayer: Gegner suchen via Matchmaking
            _main.NavigateTo(new MatchmakingView(_main));
        }

        private void Singleplayer_Click(object sender, MouseButtonEventArgs e)
        {
            // Singleplayer: Direkt zur SinglePlayer-Übersicht
            _main.StartNewGame("KI");
            _main.NavigateTo(new SinglePlayerView(_main));
        }
    }
}
