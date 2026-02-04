using System.Windows.Controls;

namespace Quizly.Views
{
    public partial class MatchmakingView : UserControl
    {
        private readonly MainWindow _main;

        public MatchmakingView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private void Back_Click(object sender, System.Windows.RoutedEventArgs e) => _main.GoHome();

        private void Found_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            // ✅ GEÄNDERT: Multiplayer korrekt starten
            _main.StartMultiplayer("Gegner123");  // Hier später echten Gegnernamen einfügen
        }
    }
}
