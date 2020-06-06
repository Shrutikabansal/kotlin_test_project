package local.myfantasytube.myapps.fourinarow

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	var playerXO = 'X'
	//Board that contains the X/O
	var board : Array<CharArray> = Array(6) { CharArray(7) }
    	//Displays who is playing	
	var turnTextVw : TextView? = null
    	//Layout that arranges its children into rows and columns
	//Used to operates on cells via code
	var tLayout : TableLayout? = null

	override fun onCreate(savedInstanceState : Bundle?) {
		//Extending parent class' method: running your code in addition 
		//to the existing code in the onCreate() 
		super.onCreate(savedInstanceState)
        	//It sets the view to be displayed and inflates the activity_main.xml
		//to display the UI 
		setContentView(R.layout.activity_main)
        	//Get a reference of toolbar	
		val tbar = findViewById(R.id.toolbar) as Toolbar
		//Set the toolbar
		setSupportActionBar(tbar)

		//Get a reference of floating action button
		val fab = findViewById(R.id.fab) as FloatingActionButton
		//Set an action for the floating action button
		fab.setOnClickListener { newGame(true) }

		//Grab the reference to TextView, which display text to the user
        	turnTextVw = findViewById(R.id.turnTextView) as TextView
        	//Grab the reference of the TableLayout
		tLayout = findViewById(R.id.table_layout) as TableLayout
        	//Initialize and start a new game	
		newGame(true)
	}

	private fun newGame(clickListener : Boolean) {
        	playerXO = 'X'
		//Reading values from the strings.xml and doing string replacement	
        	turnTextVw?.text = String.format(resources.getString(R.string.playerXO), playerXO)
		//Loop through all cells
        	for (i in 0 until board.size) {
            		for (j in 0 until board[i].size) {
                		board[i][j] = ' '
                		//Grab a cell
				val cell = (tLayout?.getChildAt(i) as TableRow).getChildAt(j) as TextView
                		//Initialize cell text
				cell.text = ""
				//Add a listener to the cell
                		if (clickListener) { cell.setOnClickListener { cellClickListener(i, j) } }
        		}
        	}
    	}

	private fun cellClickListener(row: Int, column: Int) {
		//Find the lowest available space in the column	
		if(board[0][column] == ' ') {
			var i : Int = row
			if(board[board.size - 1][column] == ' ') {
				board[board.size - 1][column] = playerXO
				i = 5 		
			}
			else {
				while(i < board.size - 1) {
                              		i++
                                	if(board[i][column] == ' ') continue
                                	else break
                        	}
				if(board[--i][column] == ' ') board[i][column] = playerXO
			}
			//Set the cell text to X or O according to who is playing	
                	((tLayout?.getChildAt(i) as TableRow).getChildAt(column) as TextView).text = playerXO.toString()
                	//Switch player
			playerXO = if ('X' == playerXO) 'O' else 'X'
                	//Display new player
			turnTextVw?.text = String.format(resources.getString(R.string.playerXO), playerXO)

			var status : String? = null
			//Check if X wins
        		if(checkWin('X', board)) { status = String.format(resources.getString(R.string.winner), 'X') }
        		//Check if O wins
			else if (checkWin('O', board)) { status = String.format(resources.getString(R.string.winner), 'O') }
        		//Check if the board is full	
			else if (noEmptyCell(board)) { status = resources.getString(R.string.draw) }

        		if (status != null) {
				//If someone wins display a message box
            			turnTextVw?.text = status
            			val builder = AlertDialog.Builder(this)
            			builder.setMessage(status)
            			builder.setPositiveButton(android.R.string.ok, { dialog, id -> newGame(false) })
            			val dialog = builder.create()
            			dialog.show()
			}
		}
    	}

    	private fun noEmptyCell(b : Array<CharArray>) : Boolean {
		for (i in 0 until b.size) {
            		for (j in 0 until b[i].size)
				if(b[i][j] == ' ') return false
        	}
        	return true
	}

	//Core algorithm that check the board for a winner
    	private fun checkWin(c : Char, board : Array<CharArray>) : Boolean {
		//Horizontal
		for(i in 0 until board.size) {
			try {
				for(x in 0 until board[i].size) {	
					if(board[i][x] == c && board[i][x + 1] == c && 
					board[i][x + 2] == c && board[i][x + 3] == c) return true
				}
			} catch(ex : ArrayIndexOutOfBoundsException) { } 
		}
	
		//Vertical
		for(i in 0 until board[0].size) {
			try {
				for(x in 0 until board.size) {
					if(board[i][x] == c && board[i + 1][x] == c &&
                                        board[i + 2][x] == c && board[i + 3][x] == c) return true
				}
			} catch(ex : ArrayIndexOutOfBoundsException) { }

			//Slant
			try {
                        	for(x in 0 until board.size) {
                                	if(board[i][x] == c && board[i + 1][x - 1] == c &&
                                	board[i + 2][x - 2] == c && board[i + 3][x - 3] == c) return true

					if(board[i][x] == c && board[i + 1][x + 1] == c && 
                                        board[i + 2][x + 2] == c && board[i + 3][x + 3] == c) return true
                                }
                         } catch(ex : ArrayIndexOutOfBoundsException) { }
		}

		return false
	}

	override fun onCreateOptionsMenu(menu : Menu): Boolean {
        	menuInflater.inflate(R.menu.menu_main, menu)
        	return true
	}

	override fun onOptionsItemSelected(item : MenuItem): Boolean {
        	return when (item.itemId) {
            		R.id.action_settings -> true
            		else -> super.onOptionsItemSelected(item)
        	}
    	}
}
