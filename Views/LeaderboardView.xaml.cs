using System.Windows.Controls;

namespace Quizly.Views
{
    public partial class LeaderboardView : UserControl
    {
        private readonly MainWindow _main;

        public LeaderboardView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private void Back_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new StartView(_main));
        }
    }
}
