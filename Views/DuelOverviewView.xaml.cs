using System.Windows.Controls;

namespace Quizly.Views
{
    public partial class DuelOverviewView : UserControl
    {
        private readonly MainWindow _main;

        public DuelOverviewView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private void Back_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new HomeView(_main));
        }

        private void Play_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new CategoryPickView(_main));
        }
    }
}
