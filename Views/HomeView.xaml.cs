using System.Windows.Controls;

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

        private void OpenMatch_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new DuelOverviewView(_main));
        }

        private void NewMatch_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            NewGameOverlay.Visibility = System.Windows.Visibility.Visible;
        }

        private void CloseOverlay_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            NewGameOverlay.Visibility = System.Windows.Visibility.Collapsed;
        }

        private void FindOpponent_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            NewGameOverlay.Visibility = System.Windows.Visibility.Collapsed;
            _main.NavigateTo(new MatchmakingView(_main));
        }

        private void PlayWithFriend_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            NewGameOverlay.Visibility = System.Windows.Visibility.Collapsed;
            _main.GoFriends();
        }
    }
}
