using System.Windows.Controls;

namespace Quizly.Views
{
    public partial class CategoryView : UserControl
    {
        private readonly MainWindow _main;

        public CategoryView(MainWindow main)
        {
            InitializeComponent();
            _main = main;
        }

        private void Back_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new StartView(_main));
        }

        private void Start_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _main.NavigateTo(new QuestionView(_main));
        }
    }
}
