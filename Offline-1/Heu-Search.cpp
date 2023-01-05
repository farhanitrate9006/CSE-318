#include "Node.h"

int inversionCount(vector<vector<int>> board, int k)
{
    int cnt = 0;
    int n = k*k;
    vector<int> tempBoard(n);

    for(int i=0; i<k; i++)
    {
        for(int j=0; j<k; j++)
        {
            int temp = i*k + j;
            tempBoard[temp] = board[i][j];
            temp++;
        }
    }

    for(int i=0; i<n-1; i++)
    {
        if(tempBoard[i] == blank)
            continue;
        for(int j=i+1; j<n; j++)
        {
            if(tempBoard[j] == blank)
                continue;
            else if(tempBoard[i] > tempBoard[j])
                cnt++;
        }
    }
    return cnt;
}

bool isSolvable(vector<vector<int>> board, int k, int blankRow)
{
    bool inversionParity = inversionCount(board, k)%2;
    if(k%2)
        return !inversionParity;
    else if(inversionParity%2 != blankRow%2)
        return true;
    return false;
}

Node getGoalNode(int k)
{
    Node goal(k, -1);
    int cnt = 1;

    for(int i=0; i<k; i++)
        for(int j=0; j<k; j++)
            goal.board[i][j] = cnt++;
    goal.board[k-1][k-1] = blank;

    return goal;
}

bool validBoard(int k, pii blankPos, char ch)
{
    int row = blankPos.row, col = blankPos.col;
    switch(ch)
    {
        case 'L':
            col--;
            break;
        case 'R':
            col++;
            break;
        case 'U':
            row--;
            break;
        case 'D':
            row++;
            break;
    }
    if(row<0 || row>=k || col<0 || col>=k)
        return false;
    return true;
}

Node createBoard(Node parent, char ch)
{
    Node temp(parent.gridSize, parent.sourceCost+1);
    temp.board = parent.board;
    temp.setBlank(parent.blankPos, ch);
    temp.setBoard(temp.blankPos, parent.blankPos);
    temp.moves += parent.moves + ch;

    return temp;
}

void printPath(Node printBoard, string& moves)
{
    cout << printBoard << endl << endl;
    for(auto &ch : moves)
    {
        printBoard = createBoard(printBoard, ch);
        cout << printBoard << endl << endl;
    }
}

template <class X>
void solve(Node initBoard, string heuristic)
{
    int k = initBoard.gridSize;
    unordered_set<Node> closeList;
    priority_queue<Node, vector<Node>, X> openList;
    openList.push(initBoard);
    Node parent;
    Node goal = getGoalNode(k);

    int explored, expanded;
    explored = expanded = 0;

    while(true)
    {
        parent = openList.top();
        openList.pop();
        explored++;
        if(parent == goal)
            break;

        if(validBoard(k, parent.blankPos, 'L'))
        {
            Node temp = createBoard(parent, 'L');
            auto it = closeList.find(temp);
            if(it == closeList.end())
            {
                openList.push(temp);
                closeList.insert(temp);
                expanded++;
            }
            else if(it->sourceCost > temp.sourceCost)
            {
                openList.push(temp);
                closeList.erase(it);
                closeList.insert(temp);
                expanded++;
            }
        }
        if(validBoard(k, parent.blankPos, 'R'))
        {
            Node temp = createBoard(parent, 'R');
            auto it = closeList.find(temp);
            if(it == closeList.end())
            {
                openList.push(temp);
                closeList.insert(temp);
                expanded++;
            }
            else if(it->sourceCost > temp.sourceCost)
            {
                openList.push(temp);
                closeList.erase(it);
                closeList.insert(temp);
                expanded++;
            }
        }
        if(validBoard(k, parent.blankPos, 'U'))
        {
            Node temp = createBoard(parent, 'U');
            auto it = closeList.find(temp);
            if(it == closeList.end())
            {
                openList.push(temp);
                closeList.insert(temp);
                expanded++;
            }
            else if(it->sourceCost > temp.sourceCost)
            {
                openList.push(temp);
                closeList.erase(it);
                closeList.insert(temp);
                expanded++;
            }
        }
        if(validBoard(k, parent.blankPos, 'D'))
        {
            Node temp = createBoard(parent, 'D');
            auto it = closeList.find(temp);
            if(it == closeList.end())
            {
                openList.push(temp);
                closeList.insert(temp);
                expanded++;
            }
            else if(it->sourceCost > temp.sourceCost)
            {
                openList.push(temp);
                closeList.erase(it);
                closeList.insert(temp);
                expanded++;
            }
        }
    }

    cout << "===== " << heuristic << " =====" << endl;
    printPath(initBoard, parent.moves);
    cout << "Minimum number of moves: " << parent.sourceCost << endl;
    cout << "Expanded nodes: " << expanded << endl;
    cout << "Explored nodes: " << explored << endl;
}

int main()
{
    int k, x;

    cin >> k;
    Node initBoard(k, 0);

    for(int i=0; i<k; i++)
    {
        for(int j=0; j<k; j++)
        {
            cin >> x;
            initBoard.board[i][j] = x;
            if(x == blank)
                initBoard.blankPos = {i, j};
        }
    }

    cout << "The puzzle is ";
    if(!isSolvable(initBoard.board, k, initBoard.blankPos.row))
        cout << "not solvable" << endl;
    else
    {
        cout << "solvable" << endl;
        solve<CompareMan>(initBoard, "Manhattan");
        solve<CompareHam>(initBoard, "Hamming");
    }
}
