using Quizly.Views;
using System.Windows.Controls;

namespace Quizly.Views
{
    public partial class StartView : UserControl
    {
        private readonly MainWindow _main;

        public StartView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private void QuickMatch_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new MatchmakingView(_main));
        }

        private void Category_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new CategoryView(_main));
        }

        private void Leaderboard_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new LeaderboardView(_main));
        }

        private void Question_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new QuestionView(_main));
        }
    }
}
