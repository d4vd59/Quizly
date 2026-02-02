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

        private void Back_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new StartView(_main));
        }

        private void Found_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new CategoryView(_main));
        }
    }
}
