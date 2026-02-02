using System.Windows.Controls;

namespace Quizly.Views
{
    public partial class ResultView : UserControl
    {
        private readonly MainWindow _main;

        public ResultView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private void Next_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new DuelOverviewView(_main));
        }
    }
}
