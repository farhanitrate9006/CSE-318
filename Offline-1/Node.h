#include<bits/stdc++.h>
using namespace std;

#ifndef NODE_H
#define NODE_H

#define pii pair<int, int>
#define row first
#define col second
#define pb push_back
const int blank = 0;

pii getRowCol(int num, int k)
{
    num--;
    return { num/k, num%k };
}

int mod(int a)
{
    if(a<0)
        return -a;
    return a;
}

class Node
{
public:
    vector<vector<int>> board;
    int gridSize, sourceCost;
    int hamVal, manVal;
    pii blankPos;
    string moves;

    Node() {}

    Node(int k, int sourceCost)
    {
        board.assign(k, vector<int>(k));
        this->gridSize = k;
        this->sourceCost = sourceCost;
        this->hamVal = 0;
        this->manVal = 0;
        this->moves = "";
    }

    void setBlank(pii parentBlankPos, char ch)
    {
        int row = parentBlankPos.row, col = parentBlankPos.col;
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
        this->blankPos = {row, col};
    }

    void setBoard(pii blankPos, pii parentBlankPos)
    {
        int temp = board[blankPos.row][blankPos.col];
        board[blankPos.row][blankPos.col] = board[parentBlankPos.row][parentBlankPos.col];
        board[parentBlankPos.row][parentBlankPos.col] = temp;

        this->hamVal = hamming();
        this->manVal = manhattan();
    }

    int hamming()
    {
        int hamVal = 0, k = gridSize;
        for(int i=0; i<k; i++)
        {
            for(int j=0; j<k; j++)
            {
                if(board[i][j] == blank)
                    continue;
                pii actualPos = getRowCol(board[i][j], k);
                hamVal += (i == actualPos.row && j == actualPos.col) ? 0 : 1;
            }
        }
        return hamVal;
    }

    int manhattan()
    {
        int manVal = 0, k = gridSize;
        for(int i=0; i<k; i++)
        {
            for(int j=0; j<k; j++)
            {
                if(board[i][j] == blank)
                    continue;
                pii actualPos = getRowCol(board[i][j], k);
                manVal += mod((i - actualPos.row)) + mod((j - actualPos.col));
            }
        }
        return manVal;
    }

    bool operator==(const Node& node) const
    {
        int k = gridSize;
        for(int i=0; i<k; i++)
            for(int j=0; j<k; j++)
                if(board[i][j] != node.board[i][j])
                    return false;
        return true;
    }

    friend ostream& operator<<(ostream& os, const Node& node);
};

ostream& operator<<(ostream& os, const Node& node)
{
    int k = node.gridSize;
    for(int i=0; i<k; i++)
    {
        for(int j=0; j<k; j++)
        {
            if(!node.board[i][j])
                os << "*" << " ";
            else
                os << node.board[i][j] << " ";
        }
            
        os << endl;
    }
    return os;
}

struct CompareMan {
    bool operator()(Node const& n1, Node const& n2) {
        return n1.sourceCost + n1.manVal > n2.sourceCost + n2.manVal;
    }
};

struct CompareHam {
    bool operator()(Node const& n1, Node const& n2) {
        return n1.sourceCost + n1.hamVal > n2.sourceCost + n2.hamVal;
    }
};

namespace std {
    template <>
    struct hash<Node> {
        std::size_t operator()(const Node& c) const {
            size_t hash_value = 0;
            for(auto& row : c.board)
                for (auto& cell : row)
                    hash_value ^= cell + 0x9e3779b9 + (hash_value << 6) + (hash_value >> 2);
            return hash_value;
        }
    };
}

#endif // HEAP_H
